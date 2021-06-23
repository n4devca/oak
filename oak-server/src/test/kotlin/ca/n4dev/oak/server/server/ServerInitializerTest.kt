package ca.n4dev.oak.server.server

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.filter.RequestLoggerFilter
import ca.n4dev.oak.server.configuration.bootstrap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */


fun main() {

    val config = bootstrap("test") {

        preFilters {
            add(RequestLoggerFilter())
        }

        endpoints {
            add(helloEndpoint())
        }
    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint(): Endpoint {
    return Endpoint("/hello/{name}", HttpMethod.GET, ContentType.JSON.value) {
        val name = it.pathVariables["name"]
        HttpResponse(Status.OK, ContentType.JSON, "{\"message\" : \"Hello $name\"}")
    }
}