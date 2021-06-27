/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.interceptor

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.http.HttpResponse


class InterceptorChain(private val interceptors: List<Interceptor>) {

    private var position = 0;

    fun next(httpContext: HttpContext): HttpResponse? {
        if (position < interceptors.size) {
            return interceptors[position++].intercept(httpContext, this)
        }

        return null
    }
}