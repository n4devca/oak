/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.routing

import ca.n4dev.endpoint.Endpoint
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse

class Router(endpoints: Set<Endpoint>) {

    private val endpointMatchers = endpoints.map {
        RouteMatcher(it)
    }

    fun getRoute(httpRequest: HttpRequest, httpResponse: HttpResponse): Route? {

        for (endpoint in endpointMatchers) {
            val matchEndpoint = endpoint.match(httpRequest.path)
            if (matchEndpoint != null) {
                return matchEndpoint
            }
        }

        return null
    }

}
