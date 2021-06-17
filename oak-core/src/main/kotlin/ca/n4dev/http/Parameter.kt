/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.http

interface Parameter {
    val name: String
    val value: List<Any>
}