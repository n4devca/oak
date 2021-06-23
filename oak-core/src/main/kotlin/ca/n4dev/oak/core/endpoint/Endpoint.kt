/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.endpoint

import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.HttpResponse

class Endpoint(
    val path: String,
    val method: HttpMethod = HttpMethod.GET,
    val accept: ContentType = ContentType.ALL,
    val handler: (httpRequest: HttpRequest) -> HttpResponse
)