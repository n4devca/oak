/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.http

import java.io.InputStream

data class HttpRequest (
    val method: HttpMethod,
    val path: String,
    val params: List<MultiParameter> = listOf(),
    val headers: List<Header> = listOf(),
    val body: InputStream? = null,
    val pathVariables: Map<String, String> = mapOf()) {

    fun header(name: String): String? {
        return this.headers.firstOrNull {
            it.name == name
        }?.value
    }

    fun param(name: String): List<Any>? {
        return this.params.firstOrNull {
            it.name == name
        }?.value
    }

    fun accepts(): List<Header> {
        return headers.filter { header -> header.name == HttpHeader.Accept }
    }
}
