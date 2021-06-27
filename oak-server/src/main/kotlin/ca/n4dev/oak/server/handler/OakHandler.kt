/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.handler

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.exception.InterceptorException
import ca.n4dev.oak.core.interceptor.InterceptorChain
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.routing.Router
import ca.n4dev.oak.server.endpoint.StandardEndpoints
import ca.n4dev.oak.server.interceptor.EndpointInterceptor
import ca.n4dev.oak.server.utils.copyToStream
import ca.n4dev.oak.server.utils.toHttpRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import org.slf4j.LoggerFactory

class OakHandler(private val router: Router,
                 private val preInterceptors: List<Interceptor> = emptyList(),
                 private val postInterceptors: List<Interceptor> = emptyList()) : AbstractHandler() {

    private val logger = LoggerFactory.getLogger(OakHandler::class.java)

    override fun handle(path: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {


        // Get Route
        val routeMatch = router.getRoute(path)
        val endpoint = routeMatch?.endpoint ?: StandardEndpoints.notFound
        val httpRequest = toHttpRequest(path, routeMatch?.pathVariables ?: mapOf(), request)
        val httpContext = HttpContext(httpRequest)

        try {

            val interceptorChain = buildChain(preInterceptors, endpoint, postInterceptors);

            val interceptorsResponse = interceptorChain.next(httpContext)

            write(selectResponse(interceptorsResponse, httpContext), response)

        } catch (interceptorException: InterceptorException) {

            // Handler...
            logger.info("Interceptor exception. Handling response...")

        } catch (exception: Exception) {
            logger.error("Unplanned exception!")
            // log exception
        }

        // Set isHandled to true for Jetty
        baseRequest.isHandled = true
    }


    private fun buildChain(preInterceptors: List<Interceptor>,
                           endpoint: Endpoint,
                           postInterceptors: List<Interceptor>): InterceptorChain {

        return InterceptorChain(
            preInterceptors + EndpointInterceptor(endpoint) + postInterceptors
        )
    }

    private fun selectResponse(httpResponse: HttpResponse?, httpContext: HttpContext): HttpResponse {

        return httpResponse ?: httpContext.httpResponse ?: StandardEndpoints.notFound.handler(httpContext)

    }

    private fun write(httpResponse: HttpResponse, response: HttpServletResponse) {

        response.status = httpResponse.status?.code ?: 500
        response.contentType = httpResponse.contentType?.value
        response.addHeader("Server", "Oak");

        httpResponse.headers.forEach {
            response.addHeader(it.name, it.value);
        }

        httpResponse.body?.let { body ->
            copyToStream(body, response.outputStream)
        }
    }
}