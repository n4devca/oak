/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.handler

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.exception.FilterException
import ca.n4dev.oak.core.filter.FilterChain
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.routing.Router
import ca.n4dev.oak.server.endpoint.StandardEndpoints
import ca.n4dev.oak.server.filter.EndpointFilter
import ca.n4dev.oak.server.utils.copyToStream
import ca.n4dev.oak.server.utils.toHttpRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import org.slf4j.LoggerFactory

class OakHandler(private val router: Router,
                 private val preFilters: List<HttpFilter> = emptyList(),
                 private val postFilters: List<HttpFilter> = emptyList()) : AbstractHandler() {

    private val logger = LoggerFactory.getLogger(OakHandler::class.java)

    override fun handle(path: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {


        // Get Route
        val routeMatch = router.getRoute(path)
        val endpoint = routeMatch?.endpoint ?: StandardEndpoints.notFound
        val httpRequest = toHttpRequest(path, routeMatch?.pathVariables ?: mapOf(), request)
        val httpContext = HttpContext(httpRequest)

        try {

            val filterChain = buildChain(preFilters, endpoint, postFilters);

            val filterResponse = filterChain.next(httpContext)

            write(selectResponse(filterResponse, httpContext), response)

        } catch (filterException: FilterException) {

            // Handler...
            logger.info("Filter exception. Handling response...")

        } catch (exception: Exception) {
            logger.error("Unplanned exception!")
            // log exception
        }

        // Set isHandled to true for Jetty
        baseRequest.isHandled = true
    }


    private fun buildChain(preFilters: List<HttpFilter>,
                           endpoint: Endpoint,
                           postFilters: List<HttpFilter>): FilterChain {

        return FilterChain(
            preFilters + EndpointFilter(endpoint) + postFilters
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