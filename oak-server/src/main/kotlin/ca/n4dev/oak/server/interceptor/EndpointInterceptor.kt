/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.interceptor

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.interceptor.InterceptorChain
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.http.HttpResponse

class EndpointInterceptor(private val endpoint: Endpoint, override val name: String = "EndpointInterceptor") : Interceptor {
    override fun intercept(httpContext: HttpContext, chain: InterceptorChain): HttpResponse? {
        val httpResponse = endpoint.handler(httpContext)
        httpContext.httpResponse = httpResponse
        return chain.next(httpContext)
    }
}