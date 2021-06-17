/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http

interface HttpResponse<T> {
    val status: Status
    val headers: List<Header>
    val body: T?
}