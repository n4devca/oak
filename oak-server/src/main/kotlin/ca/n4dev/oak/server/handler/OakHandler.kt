/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.handler

import ca.n4dev.oak.core.exception.FilterException
import ca.n4dev.oak.core.filter.PostFilter
import ca.n4dev.oak.core.filter.PreFilter
import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.routing.Router
import ca.n4dev.oak.plugin.endpoint.NotFoundEndpoint
import ca.n4dev.oak.server.utils.copyToStream
import ca.n4dev.oak.server.utils.toHttpRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import org.slf4j.LoggerFactory

class OakHandler(private val router: Router,
                 private val preFilters: List<PreFilter> = emptyList(),
                 private val postFilters: List<PostFilter> = emptyList()) : AbstractHandler() {

    private val logger = LoggerFactory.getLogger(OakHandler::class.java)

    override fun handle(path: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {

        val httpRequest = toHttpRequest(path, request)

        // Get Route
        val routeMatch = router.getRoute(httpRequest)
        val endpoint = routeMatch?.endpoint ?: NotFoundEndpoint
        routeMatch?.pathVariables?.toMutableMap()?.let {
            httpRequest.pathVariables = it
        }

        try {

            val filteredRequest = applyPreFilters(httpRequest)

            // Handling
            val handlerResponse= endpoint.handler(filteredRequest)

            // postProcessing
            val filteredResponse = applyPostFilters(filteredRequest, handlerResponse)

            // Write
            write(filteredResponse, response)

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

    /**
     * Apply all PreFilter each time (possibly) modifying the httpRequest.
     */
    private fun applyPreFilters(httpRequest: HttpRequest): HttpRequest {
        return preFilters.fold(httpRequest) { req, preFilter ->
            preFilter.intercept(req)
        }
    }

    /**
     * Apply all PostFilter each time (possibly) modifying the HttpResponse.
     */
    private fun applyPostFilters(httpRequest: HttpRequest, httpResponse: HttpResponse): HttpResponse {
        return postFilters.fold(httpResponse) { response, postFilter ->
            postFilter.intercept(httpRequest, response)
        }
    }

    private fun write(httpResponse: HttpResponse, response: HttpServletResponse) {

        response.status = httpResponse.status.code
        response.contentType = httpResponse.contentType.value

        httpResponse.body?.let { body ->
            copyToStream(body, response.outputStream)
        }
    }
}