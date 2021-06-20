/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http.filter

import ca.n4dev.filter.Filter
import ca.n4dev.filter.FilterChain
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse
import ca.n4dev.http.Status

class HelloFilter : Filter {

    override fun intercept(httpRequest: HttpRequest, httpResponse: HttpResponse): FilterChain {
        return FilterChain.Completed(httpResponse.copy(status = Status.OK, body = "Hello World"))
    }
}