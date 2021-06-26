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

    override fun validate(hash: String, password: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}