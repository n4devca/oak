/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.server

import ca.n4dev.configuration.bootstrap
import ca.n4dev.http.filter.RequestLogger

fun main() {

    ServerInitializer.start(
        bootstrap("test") {

            filters {
                RequestLogger()
            }

            endpoints {

            }
        }.build()
    )
}
