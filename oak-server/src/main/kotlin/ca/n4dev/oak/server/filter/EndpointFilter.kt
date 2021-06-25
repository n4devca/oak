/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.filter

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.filter.FilterChain
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.http.HttpResponse

class EndpointFilter(private val endpoint: Endpoint, override val name: String = "EndpointFilter") : HttpFilter {
    override fun intercept(httpContext: HttpContext, chain: FilterChain): HttpResponse? {
        val httpResponse = endpoint.handler(httpContext)
        httpContext.httpResponse = httpResponse
        return chain.next(httpContext)
    }
}