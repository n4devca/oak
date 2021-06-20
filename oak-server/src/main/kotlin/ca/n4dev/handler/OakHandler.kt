/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.handler

import ca.n4dev.routing.Match
import ca.n4dev.routing.Router
import ca.n4dev.utils.toHttpRequest
import ca.n4dev.utils.toHttpResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import org.slf4j.LoggerFactory

class OakHandler(private val router: Router) : AbstractHandler() {

    private val logger = LoggerFactory.getLogger(OakHandler::class.java)

    //private val routingHandler = RoutingHandler()

    override fun handle(path: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {

        val httpRequest = toHttpRequest(path, request)
        val httpResponse = toHttpResponse(response)

        val routeMatch = router.getRoute(httpRequest, httpResponse)

        when (routeMatch) {

            is Match.True -> {

            }

            else -> {

            }
        }
    }

    private fun routing() {

    }

    private fun security() {

    }

    private fun preProcessing() {

    }

    private fun endpoint() {

    }

    private fun postProcessing() {

    }
}