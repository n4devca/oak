/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http


data class HttpResponse(
    val status: Status? = null,
    val headers: List<Header> = listOf(),
    val body: String? = null)