/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.endpoint

import ca.n4dev.endpoint.Endpoint
import ca.n4dev.http.ContentType
import ca.n4dev.http.HttpMethod
import ca.n4dev.http.HttpResponse
import ca.n4dev.http.Status

val NotFoundEndpoint = Endpoint("", HttpMethod.ANY, ContentType.ALL.value) { httpRequest, httpResponse ->
    HttpResponse(Status.NOT_FOUND)
}