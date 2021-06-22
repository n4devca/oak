package ca.n4dev.routing

import ca.n4dev.endpoint.Endpoint
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
internal class RouteMatcherTest {

    private fun getEndpoint(path: String) = Endpoint(path) { _, resp -> resp}


    @Test
    fun `should match the url`() {
        val routerMatcher = RouteMatcher(getEndpoint("/api/users/{userName}/profile/{profileName}"))

        assertNotNull(routerMatcher.match("/api/users/bob/profile/public"))
        assertNotNull(routerMatcher.match("/api/users/bob-42/profile/public"))
        assertNotNull(routerMatcher.match("/api/users/bob@42.tld/profile/public"))
    }


    @Test
    fun `should not match the url`() {
        val routerMatcher = RouteMatcher(getEndpoint("/api/users/{userName}/profile/{profileName}"))

        assertNull(routerMatcher.match("/api/users/profile/public"))
        assertNull(routerMatcher.match("/api/users/bob-42/profile"))
        assertNull(routerMatcher.match("/api/users/bob@42.tld/profile/"))
    }

    @Test
    fun `should have the parameters`() {
        val routerMatcher = RouteMatcher(getEndpoint("/api/users/{userName}/profile/{profileName}"))

        val route1 = routerMatcher.match("/api/users/bob/profile/public")
        assertRouteMatching(route1,  mapOf("userName" to "bob", "profileName" to "public"))

        val route2 = routerMatcher.match("/api/users/bob-42/profile/public")
        assertRouteMatching(route2,  mapOf("userName" to "bob-42", "profileName" to "public"))

        val route3 = routerMatcher.match("/api/users/bob@42.tld/profile/public")
        assertRouteMatching(route3,  mapOf("userName" to "bob@42.tld", "profileName" to "public"))

        val route4 = routerMatcher.match("/api/users/bob/profile/public-logo.jpg")
        assertRouteMatching(route4,  mapOf("userName" to "bob", "profileName" to "public-logo.jpg"))
    }

    @Test
    fun `parameters should be decoded`() {
        val routerMatcher = RouteMatcher(getEndpoint("/api/users/{userName}/profile/{profileName}"))

        assertRouteMatching(routerMatcher.match("/api/users/bob%40gg/profile/public"),
             mapOf("userName" to "bob@gg", "profileName" to "public"))

        assertRouteMatching(routerMatcher.match("/api/users/bob/profile/public%20life"),
             mapOf("userName" to "bob", "profileName" to "public life"))

        assertRouteMatching(routerMatcher.match("/api/users/bob/profile/public%2Blife"),
             mapOf("userName" to "bob", "profileName" to "public+life"))
    }


    private fun assertRouteMatching(routeMatch: Route?, pathVariable: Map<String, String>) {


        if (routeMatch != null) {
            assertEquals(pathVariable.size, routeMatch.pathVariables.size)

            pathVariable.forEach { (k, v) ->
                assertEquals(v, routeMatch.pathVariables[k])
            }

        } else {
            fail("Route not matching")
        }

    }
}