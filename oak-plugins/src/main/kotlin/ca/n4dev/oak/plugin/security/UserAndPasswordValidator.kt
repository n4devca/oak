/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security

class UserAndPasswordValidator(private val userService: UserService,
                               private val passwordHasher: PasswordHasher) {

    fun validate(usernameAndPassword: UserAndPassword): Boolean {

        return try {

            userService.getUser(usernameAndPassword.userName)?.let { user ->
                user.enabled && passwordHasher.validate(user.password, usernameAndPassword.password)
            } ?: false

        } catch (exception: Exception) {
            false
        }
    }
}