/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http

import ca.n4dev.filter.Filter
import ca.n4dev.filter.FilterChain

class RequestLogger : Filter {
    override fun intercept(httpRequest: HttpRequest<*>?, httpResponse: HttpResponse<*>?) {
        println("RequestLogger!")
    }
}