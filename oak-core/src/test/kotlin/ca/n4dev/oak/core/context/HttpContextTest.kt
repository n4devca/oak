package ca.n4dev.oak.core.context

import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.MultiParameter
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
class HttpContextTest {

    @Test
    internal fun `non existing header should return null`() {
        val req = HttpRequest(HttpMethod.GET, "/test")

        val ctx = HttpContext(req)

        assertNull(ctx.header("unknown"))
    }

    @Test
    fun `existing header should be returned`() {
        val req = HttpRequest(HttpMethod.GET, "/test", headers = listOf(Header("X-Header", "42")))

        val ctx = HttpContext(req)

        assertEquals("42", ctx.header("X-Header"))
    }

    @Test
    fun `non existing parameter should return null`() {
        val req = HttpRequest(HttpMethod.GET, "/test", listOf(MultiParameter("locale", listOf("fr", "en"))))

        val ctx = HttpContext(req)

        assertNull(ctx.param("page"))
    }

    @Test
    fun `existing parameter should be returned`() {
        val req = HttpRequest(HttpMethod.GET,
                        "/test",
                            listOf(MultiParameter("locale", listOf("fr", "en"))),
                            listOf(Header("X-Header", "42")))

        val ctx = HttpContext(req)

        val localeParam = ctx.param("locale")
        assertNotNull(localeParam)
        assertTrue(localeParam!!.all { param -> param == "fr" || param == "en" })
    }

    @Test
    fun `accepts() should return all Accept headers`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "*/*, application/json")))

        val ctx = HttpContext(req)

        val accepts = ctx.accepts()
        assertEquals(2, accepts.size)
        assertTrue(accepts.all {
            it == "*/*" || it == "application/json"
        })
    }

    @Test
    fun `should accept JSON`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "application/json")))
        val ctx = HttpContext(req)
        assertTrue(ctx.isAcceptingJson())
    }

    @Test
    fun `should accept HTML`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "text/html")))
        val ctx = HttpContext(req)
        assertTrue(ctx.isAcceptingHTML())
    }

    @Test
    fun `joker should accept JSON`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "*/*")))
        val ctx = HttpContext(req)
        assertTrue(ctx.isAcceptingJson())
    }

    @Test
    fun `joker should accept HTML`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "*/*")))
        val ctx = HttpContext(req)
        assertTrue(ctx.isAcceptingHTML())
    }

    @Test
    fun `should not accept JSON`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "text/plain")))
        val ctx = HttpContext(req)
        assertFalse(ctx.isAcceptingJson())
    }

    @Test
    fun `should not accept HTML`() {
        val req = HttpRequest(HttpMethod.GET, "/test",
            headers = listOf(Header("X-Header", "42"), Header("Accept", "text/xml")))
        val ctx = HttpContext(req)
        assertFalse(ctx.isAcceptingHTML())
    }
}