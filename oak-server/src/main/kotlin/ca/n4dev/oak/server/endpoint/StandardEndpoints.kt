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

    val respondAsText = httpContext.httpRequest.accepts().any { header ->
        header.value.contains(ContentType.HTML.value) ||
                header.value.contains(ContentType.TEXT.value)
    }

    if (respondAsText) {
        HttpResponse(status, ContentType.HTML)
    } else {
        HttpResponse(status, ContentType.JSON)
    }
}




object StandardEndpoints {
    val notFound = createEndpoint(Status.NOT_FOUND)
    val internalError = createEndpoint(Status.INTERNAL_SERVER_ERROR)

}