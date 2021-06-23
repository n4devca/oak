/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.endpoint

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status

val NotFoundEndpoint = Endpoint("", HttpMethod.ANY, ContentType.ALL.value) { httpRequest ->

    val respondAsText = httpRequest.accepts().any { header ->
        header.value.contains(ContentType.HTML.value) ||
                header.value.contains(ContentType.TEXT.value)
    }

    if (respondAsText) {
        HttpResponse(Status.NOT_FOUND, ContentType.HTML)
    } else {
        HttpResponse(Status.NOT_FOUND, ContentType.JSON)
    }

}