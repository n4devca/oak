/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.configuration

class OakConfig(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1") {

}