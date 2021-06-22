/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http

data class HttpRequest (
    val method: HttpMethod,
    val path: String,
    val params: List<MultiParameter> = listOf(),
    val headers: List<Header> = listOf(),
    val pathVariables: Map<String, String> = emptyMap(),
    val body: Any? = null) {

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
}
