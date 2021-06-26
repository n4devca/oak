/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status

fun createUnauthorizedResponse(httpContext: HttpContext): HttpResponse {
    val contentType = if (httpContext.isAcceptingJson()) { ContentType.JSON } else { ContentType.HTML }
    return HttpResponse(Status.UNAUTHORIZED, contentType)
}