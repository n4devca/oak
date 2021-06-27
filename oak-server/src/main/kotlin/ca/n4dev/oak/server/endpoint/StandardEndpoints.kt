/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.endpoint

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status


private fun createEndpoint(status: Status) = Endpoint("*", HttpMethod.ANY, ContentType.ALL) { httpContext ->

    when {
        httpContext.isAcceptingHTML() -> {
            HttpResponse(status, ContentType.HTML)
        }
        httpContext.isAcceptingJson() -> {
            HttpResponse(status, ContentType.JSON)
        }
        else -> {
            HttpResponse(status, ContentType.TEXT)
        }
    }
}




object StandardEndpoints {
    val notFound = createEndpoint(Status.NOT_FOUND)
    val internalError = createEndpoint(Status.INTERNAL_SERVER_ERROR)

}