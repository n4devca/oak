/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.filter

import ca.n4dev.filter.Filter
import ca.n4dev.filter.FilterChain
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse
import org.slf4j.LoggerFactory


class RequestLogger(prefix: String = "") : Filter {
    private val logger = LoggerFactory.getLogger(RequestLogger::class.java)
    private val msgPrefix: String = if (prefix.isNullOrBlank()) { "" } else { "$prefix "}

    override fun intercept(httpRequest: HttpRequest, httpResponse: HttpResponse): FilterChain {
        logger.info("${msgPrefix}${httpRequest.path}")
        return FilterChain.Next(httpResponse)
    }

}