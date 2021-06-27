/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security.hash

import ca.n4dev.oak.plugin.security.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

class BcryptPasswordHasher: PasswordHasher {

    override fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    override fun validate(plainPassword: String, hashed: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashed)
    }
}