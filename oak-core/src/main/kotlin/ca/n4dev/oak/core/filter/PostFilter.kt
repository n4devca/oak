/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.filter

import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.HttpResponse

interface PostFilter : Filter {

    fun intercept(httpRequest: HttpRequest, httpResponse: HttpResponse): HttpResponse
}