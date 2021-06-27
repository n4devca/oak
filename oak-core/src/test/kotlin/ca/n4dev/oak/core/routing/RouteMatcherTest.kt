package ca.n4dev.oak.core.routing

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
internal class RouteMatcherTest {

    private fun getEndpoint(path: String) = Endpoint(path) { _ -> HttpResponse(Status.OK, ContentType.JSON) }


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
    fun `should only match endpoint with ContentType`() {
        val endpoint = Endpoint("/api/users/{userName}", producing = ContentType.JSON) { _ -> HttpResponse(Status.OK, ContentType.JSON) }
        val routerMatcher = RouteMatcher(endpoint)

        assertNotNull(routerMatcher.match("/api/users/bob", HttpMethod.ANY, ContentType.JSON))
        assertNull(routerMatcher.match("/api/users/bob-42", HttpMethod.ANY, ContentType.HTML))
        assertNull(routerMatcher.match("/api/users/bob/profile/", HttpMethod.ANY, ContentType.JSON))

    }

    @Test
    fun `should only match endpoint with Method`() {
        val getEndpoint = Endpoint("/api/users/{userName}", producing = ContentType.JSON) { _ -> HttpResponse(Status.OK, ContentType.JSON) }
        val getRouterMatcher = RouteMatcher(getEndpoint)

        assertNotNull(getRouterMatcher.match("/api/users/bob", HttpMethod.ANY, ContentType.JSON))
        assertNotNull(getRouterMatcher.match("/api/users/bob", HttpMethod.GET, ContentType.JSON))
        assertNull(getRouterMatcher.match("/api/users/bob", HttpMethod.POST, ContentType.JSON))

        val anyEndpoint = Endpoint("/api/users/{userName}", HttpMethod.ANY, ContentType.JSON) { _ -> HttpResponse(Status.OK, ContentType.JSON) }
        val anyRouterMatcher = RouteMatcher(anyEndpoint)

        assertNotNull(anyRouterMatcher.match("/api/users/bob", HttpMethod.ANY, ContentType.JSON))
        assertNotNull(anyRouterMatcher.match("/api/users/bob", HttpMethod.GET, ContentType.JSON))
        assertNotNull(anyRouterMatcher.match("/api/users/bob", HttpMethod.POST, ContentType.JSON))

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