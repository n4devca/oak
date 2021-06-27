/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security

interface PasswordHasher {

    /**
     * @return A password hashed.
     */
    fun hash(password: String): String

    /**
     * Validate if an hash match this password
     * @param plainPassword A password to test.
     * @param hashed The hash (probably coming from your persistence)
     */
    fun validate(plainPassword: String, hashed: String): Boolean

}