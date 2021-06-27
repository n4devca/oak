/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.routing

import ca.n4dev.oak.core.endpoint.Endpoint

class Router(endpoints: Set<Endpoint>) {

    private val endpointMatchers = endpoints.map {
        RouteMatcher(it)
    }

    fun getRoute(path: String): Route? {

        for (endpoint in endpointMatchers) {
            val matchEndpoint = endpoint.match(path)
            if (matchEndpoint != null) {
                return matchEndpoint
            }
        }

        return null
    }

}
