/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.server

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.filter.FilterChain
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.filter.RequestLoggerFilter
import ca.n4dev.oak.server.configuration.bootstrap


fun main() {

    val config = bootstrap("test-server") {

        preFilters {
            add(RequestLoggerFilter())
            add(enhancedBobFilter())
        }

        endpoints {
            add(helloEndpoint())
        }

    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint() = Endpoint("/hello/{name}") {
    val name = it.httpRequest.pathVariables["name"]
    HttpResponse(Status.OK, ContentType.TEXT, "Hello $name")
}

private fun enhancedBobFilter() = object : HttpFilter {
    override fun intercept(httpContext: HttpContext, chain: FilterChain): HttpResponse? {

        if (httpContext.httpRequest.pathVariables["name"] == "Bob") {
            return HttpResponse(Status.OK, ContentType.TEXT, "Holy Crap! Hello Bob", mutableListOf(Header("X-Awesome", "It's Bob")), true)
        }

        return chain.next(httpContext)
    }

    override val name: String get() = "enhancedBobFilter"

}