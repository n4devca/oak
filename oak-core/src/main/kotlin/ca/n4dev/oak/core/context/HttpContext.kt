/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.context

import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.HttpResponse

class HttpContext(val httpRequest: HttpRequest, var httpResponse: HttpResponse? = null) {
    var userName: String? = null
}