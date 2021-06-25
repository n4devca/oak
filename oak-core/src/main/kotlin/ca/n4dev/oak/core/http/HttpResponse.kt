/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.http


class HttpResponse (
    var status: Status? = null,
    var contentType: ContentType? = null,
    var body: String? = null,
    val headers: MutableList<Header> = mutableListOf(),
    var final: Boolean = false
)