/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.filter

import ca.n4dev.oak.core.filter.PreFilter
import ca.n4dev.oak.core.http.HttpRequest
import org.slf4j.LoggerFactory

/**
 * RequestLoggerFilter.
 * Log the requested path.
 */
class RequestLoggerFilter(override val name: String = "RequestLogger") : PreFilter {
    private val logger = LoggerFactory.getLogger(RequestLoggerFilter::class.java)

    override fun intercept(httpRequest: HttpRequest): HttpRequest {
        logger.info("${httpRequest.path}")
        return httpRequest
    }
}