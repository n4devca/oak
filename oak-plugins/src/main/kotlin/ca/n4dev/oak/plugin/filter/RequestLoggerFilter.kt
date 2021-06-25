/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.filter

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.filter.FilterChain
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.http.HttpResponse
import org.slf4j.LoggerFactory

/**
 * RequestLoggerFilter.
 * Log the requested path.
 */
class RequestLoggerFilter(override val name: String = "RequestLogger") : HttpFilter {

    private val logger = LoggerFactory.getLogger(RequestLoggerFilter::class.java)

    override fun intercept(httpContext: HttpContext, chain: FilterChain): HttpResponse? {
        logger.info("${httpContext.httpRequest.path}")
        return chain.next(httpContext)
    }
}