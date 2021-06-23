/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.http


data class HttpResponse(
    val status: Status,
    val contentType: ContentType,
    val body: String? = null,
    val headers: List<Header> = listOf(),
)