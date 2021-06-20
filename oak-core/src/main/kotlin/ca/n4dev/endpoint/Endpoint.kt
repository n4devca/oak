/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.endpoint

import ca.n4dev.http.HttpMethod
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse

interface Endpoint {
    val method: HttpMethod
    val path: String
    val accept: String
    val handler: (httpRequest: HttpRequest, httpResponse: HttpResponse) -> HttpResponse
}