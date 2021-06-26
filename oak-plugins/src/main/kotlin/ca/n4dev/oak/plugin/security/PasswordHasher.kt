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
     * @param plain A password to test.
     * @param hash The hash (probably coming from your persistence)
     */
    fun validate(plain: String, hash: String): Boolean

}