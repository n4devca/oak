/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.filter

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.http.HttpResponse


class FilterChain(private val filters: List<HttpFilter>) {

    private var position = 0;

    fun next(httpContext: HttpContext): HttpResponse? {
        if (position < filters.size) {
            return filters[position++].intercept(httpContext, this)
        }

        return null
    }
}