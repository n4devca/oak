/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security

interface User {

    val userName: String

    val password: String

    val enabled: Boolean

}