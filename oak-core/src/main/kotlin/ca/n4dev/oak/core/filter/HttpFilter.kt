/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.filter

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.http.HttpResponse

interface HttpFilter {

    val name: String

    fun intercept(httpContext: HttpContext, chain: FilterChain): HttpResponse?
}