package ca.n4dev.oak.core.interceptor

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.HttpResponse
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
internal class InterceptorChainTest {


    @Test
    fun `should traverse all interceptors`() {

        val interceptors = (1..3).map { createInterceptor(it.toString()) }
        val httpContext = HttpContext(HttpRequest(HttpMethod.GET, "/test"))
        val interceptorChain = InterceptorChain(interceptors)

        val httpResponse = interceptorChain.next(httpContext)

        assertNotNull(httpResponse)

    }

    @Test
    fun `should stop when one interceptor return a response`() {

    }

    @Test
    fun `should handle exception during chain`() {

    }


    private fun createInterceptor(name: String): Interceptor {
        return object : Interceptor {
            override val name: String
                get() = name

            override fun intercept(httpContext: HttpContext, chain: InterceptorChain): HttpResponse? {
                val response = httpContext.httpResponse ?: HttpResponse()
                response.headers.add(Header("X-Filter", name))
                httpContext.httpResponse = response

                return chain.next(httpContext)
            }

        }
    }
}