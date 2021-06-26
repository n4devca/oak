/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.security

interface UserService {

    /**
     * @param userName The user's name.
     * @return A user by userName
     */
    fun getUser(userName: String): User?
}