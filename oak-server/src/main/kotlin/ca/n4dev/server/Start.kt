/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.server

import ca.n4dev.configuration.OakConfig

fun main(args: Array<String>) {
    val config = OakConfig("test")
    ServerInitializer.start(config)
}
