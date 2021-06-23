/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.filter

import ca.n4dev.oak.core.http.HttpResponse

sealed class FilterChain {
    data class Next(val httpResponse: HttpResponse) : FilterChain()
    data class Completed(val httpResponse: HttpResponse) : FilterChain()
}