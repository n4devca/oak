/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http

interface HttpRequest<T> {
    val path: String
    val parameters: List<Parameter>
    val headers: List<Header>
    val body: T?
}

