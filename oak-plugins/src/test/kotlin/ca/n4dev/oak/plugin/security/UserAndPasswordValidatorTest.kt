package ca.n4dev.oak.plugin.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
internal class UserAndPasswordValidatorTest {

    var userAndPasswordValidator: UserAndPasswordValidator =
        UserAndPasswordValidator(createUserService(), createPasswordHasher());

    @Test
    internal fun `should validate the user and password`() {
        assertTrue(userAndPasswordValidator.validate(UserAndPassword("Bob", "BobPassword")))
    }


    private fun createUserService(): UserService {
        return object : UserService {
            override fun getUser(userName: String): User? {
                return if (userName == "Bob") {
                    object : User {
                        override val userName: String
                            get() = "Bob"
                        override val password: String
                            get() = "BobPassword"
                        override val enabled: Boolean
                            get() = true
                    }
                } else if (userName == "Julie") {
                    object : User {
                        override val userName: String
                            get() = "Julie"
                        override val password: String
                            get() = "JuliePassword"
                        override val enabled: Boolean
                            get() = false
                    }
                } else {
                    null
                }
            }

        }
    }

    private fun createPasswordHasher(): PasswordHasher {
        return object : PasswordHasher {
            override fun hash(password: String): String = password

            override fun validate(plainPassword: String, hash: String): Boolean {
                return plainPassword == hash
            }

        }
    }
}