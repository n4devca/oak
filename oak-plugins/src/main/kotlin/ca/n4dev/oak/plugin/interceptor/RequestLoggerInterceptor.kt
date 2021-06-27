/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.interceptor

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.interceptor.InterceptorChain
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.http.HttpResponse
import org.slf4j.LoggerFactory

/**
 * RequestLoggerInterceptor.
 * Log the requested path.
 */
class RequestLoggerInterceptor(override val name: String = "RequestLoggerInterceptor") : Interceptor {

    private val logger = LoggerFactory.getLogger(RequestLoggerInterceptor::class.java)

    override fun intercept(httpContext: HttpContext, chain: InterceptorChain): HttpResponse? {
        logger.info("${httpContext.httpRequest.path}")
        return chain.next(httpContext)
    }
}