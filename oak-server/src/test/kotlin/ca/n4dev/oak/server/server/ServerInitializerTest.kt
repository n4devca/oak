/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.server

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.interceptor.InterceptorChain
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.configuration.Plugin
import ca.n4dev.oak.plugin.interceptor.RequestLoggerInterceptor
import ca.n4dev.oak.plugin.security.User
import ca.n4dev.oak.plugin.security.UserService
import ca.n4dev.oak.server.configuration.bootstrap


fun main() {

    val config = bootstrap("test-server") {

        preInterceptors {
            add(RequestLoggerInterceptor())
            add(enhancedBobInterceptor())
        }

        endpoints {
            add(helloEndpoint())
        }

        plugin {
            add(Plugin.basicAuth(userService))
        }

    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint() = Endpoint("/hello/{name}") {
    val name = it.httpRequest.pathVariables["name"]
    HttpResponse(Status.OK, ContentType.TEXT, "Hello $name")
}

private fun enhancedBobInterceptor() = object : Interceptor {
    override fun intercept(httpContext: HttpContext, chain: InterceptorChain): HttpResponse? {

        if (httpContext.httpRequest.pathVariables["name"] == "Bob") {
            return HttpResponse(
                Status.OK,
                ContentType.TEXT,
                "Holy Crap! Hello Bob",
                mutableListOf(Header("X-Awesome", "It's Bob")),
                true
            )
        }

        return chain.next(httpContext)
    }

    override val name: String get() = "enhancedBobInterceptor"

}

private val userService = object : UserService {
    override fun getUser(userName: String): User? {
        return if (userName == "Bob") {
            object : User {
                override val userName: String
                    get() = "Bob"
                override val password: String
                    get() = "\$2a\$10\$LrCwMNhqBZW3FIDGobeyxuHVKlOGUb84jo8RpvIpcHVBH6OFq/UcG"
                override val enabled: Boolean
                    get() = true

            }
        } else {
            null
        }
    }
}
