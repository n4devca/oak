package ca.n4dev.routing

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
internal class RouteMatcherTest {

    @Test
    fun `should match the url`() {
        val routerMatcher = RouteMatcher("/api/users/{userName}/profile/{profileName}")

        assertTrue(routerMatcher.match("/api/users/bob/profile/public") is Match.True)
        assertTrue(routerMatcher.match("/api/users/bob-42/profile/public") is Match.True)
        assertTrue(routerMatcher.match("/api/users/bob@42.tld/profile/public") is Match.True)
    }


    @Test
    fun `should not match the url`() {
        val routerMatcher = RouteMatcher("/api/users/{userName}/profile/{profileName}")

        assertTrue(routerMatcher.match("/api/users/profile/public") is Match.False)
        assertTrue(routerMatcher.match("/api/users/bob-42/profile") is Match.False)
        assertTrue(routerMatcher.match("/api/users/bob@42.tld/profile/") is Match.False)
    }

    @Test
    fun `should have the parameters`() {
        val routerMatcher = RouteMatcher("/api/users/{userName}/profile/{profileName}")

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
        val routerMatcher = RouteMatcher("/api/users/{userName}/profile/{profileName}")

        assertRouteMatching(routerMatcher.match("/api/users/bob%40gg/profile/public"),
             mapOf("userName" to "bob@gg", "profileName" to "public"))

        assertRouteMatching(routerMatcher.match("/api/users/bob/profile/public%20life"),
             mapOf("userName" to "bob", "profileName" to "public life"))

        assertRouteMatching(routerMatcher.match("/api/users/bob/profile/public%2Blife"),
             mapOf("userName" to "bob", "profileName" to "public+life"))
    }


    private fun assertRouteMatching(routeMatch: Match, pathVariable: Map<String, String>) {


        if (routeMatch is Match.True) {
            assertEquals(pathVariable.size, routeMatch.pathVariables.size)

            pathVariable.forEach { (k, v) ->
                assertEquals(v, routeMatch.pathVariables[k])
            }

        } else {
            fail("Route not matching")
        }

    }
}