/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.filter

import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse

interface FilterChain {
    fun next(httpRequest: HttpRequest<*>, httpResponse: HttpResponse<*>)
}