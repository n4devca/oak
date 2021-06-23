/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.server

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.filter.RequestLoggerFilter
import ca.n4dev.oak.server.configuration.bootstrap


fun main() {

    val config = bootstrap("test-server") {

        preFilters {
            add(RequestLoggerFilter())
        }

        endpoints {
            add(helloEndpoint())
        }

    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint() = Endpoint("/hello/{name}") {
    val name = it.pathVariables["name"]
    HttpResponse(Status.OK, ContentType.TEXT, "Hello $name")
}