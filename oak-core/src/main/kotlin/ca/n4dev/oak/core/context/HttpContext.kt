/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.context

import ca.n4dev.oak.core.http.*

class HttpContext(val httpRequest: HttpRequest, var httpResponse: HttpResponse? = null) {
    var userName: String? = null

    fun header(name: String): String? {
        return httpRequest.headers.firstOrNull {
            it.name == name
        }?.value
    }

    fun param(name: String): List<String>? {
        return httpRequest.params.firstOrNull {
            it.name == name
        }?.value
    }

    fun accepts(): List<String> {
        val acceptHeader = httpRequest.headers.filter { header -> header.name == HttpHeader.Accept }

        return acceptHeader.flatMap {
            it.value.split(",")
        }.map { it.trim() }
    }

    fun isAcceptingJson(): Boolean = accepts().any { accept ->
        accept.contains(ContentType.JSON.value) || accept.contains(ContentType.ALL.value)
    }

    fun isAcceptingHTML(): Boolean = accepts().any { accept ->
        accept.contains(ContentType.HTML.value) || accept.contains(ContentType.ALL.value)
    }
}