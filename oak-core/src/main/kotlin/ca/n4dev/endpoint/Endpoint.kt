/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.endpoint

import ca.n4dev.http.ContentType
import ca.n4dev.http.HttpMethod
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse

class Endpoint(
    val path: String,
    val method: HttpMethod = HttpMethod.GET,
    val accept: String = ContentType.ALL.value,
    val handler: (httpRequest: HttpRequest, httpResponse: HttpResponse) -> HttpResponse
)