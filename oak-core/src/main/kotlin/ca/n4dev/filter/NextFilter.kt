/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.filter

import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse

abstract class NextFilter(private val interceptor: (httpRequest: HttpRequest, httpResponse: HttpResponse) -> HttpResponse) : Filter {

    override fun intercept(httpRequest: HttpRequest, httpResponse: HttpResponse): FilterChain {
        return FilterChain.Next(interceptor(httpRequest, httpResponse))
    }
}