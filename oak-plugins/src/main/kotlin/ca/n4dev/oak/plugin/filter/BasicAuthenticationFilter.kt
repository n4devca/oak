/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.filter

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.filter.FilterChain
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.http.HttpHeader
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.plugin.security.UserAndPassword
import ca.n4dev.oak.plugin.security.UserAndPasswordValidator
import ca.n4dev.oak.plugin.security.createUnauthorizedResponse
import java.util.*

class BasicAuthenticationFilter(private val validator: UserAndPasswordValidator,
                                override val name: String = "BasicAuthenticationFilter") : HttpFilter {

    override fun intercept(httpContext: HttpContext, chain: FilterChain): HttpResponse? {

        val authorizationHeader = httpContext.header(HttpHeader.Authorization)
        val userAndPassword: UserAndPassword? = extractUser(authorizationHeader)

        return if (userAndPassword != null && validator.authenticate(userAndPassword)) {
            httpContext.userName = userAndPassword.userName
            chain.next(httpContext)
        } else {
            createUnauthorizedResponse(httpContext)
        }
    }

    private fun extractUser(authorization: String?): UserAndPassword? {

        return if (authorization != null) {
            return extractToken(authorization)?.let { token ->
                decode(token)?.split(":")?.let { usernameAndPassword ->
                    if (usernameAndPassword.size == 2) {
                        UserAndPassword(usernameAndPassword[0], usernameAndPassword[1])
                    } else {
                        null
                    }
                }
            }
        } else {
            null
        }
    }

    private fun extractToken(authorization: String): String? {
        return if (authorization.startsWith("Basic ")) {
            authorization.removePrefix("Basic ")
        } else {
            null
        }
    }

    private fun decode(token: String): String? {
        return try {
            val decodeStringByte = Base64.getDecoder().decode(token)
            return String(decodeStringByte)
        } catch (exception: Exception) {
            null
        }
    }
}


