/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.configuration

import ca.n4dev.oak.core.interceptor.PluginInterceptor
import ca.n4dev.oak.plugin.interceptor.BasicAuthenticationInterceptor
import ca.n4dev.oak.plugin.security.UserAndPasswordValidator
import ca.n4dev.oak.plugin.security.UserService
import ca.n4dev.oak.plugin.security.hash.BcryptPasswordHasher

object Plugin {

    fun basicAuth(userService: UserService): PluginInterceptor {
        val userAndPasswordValidator = UserAndPasswordValidator(userService, BcryptPasswordHasher())
        return PluginInterceptor.PreInterceptor(0, BasicAuthenticationInterceptor(userAndPasswordValidator))
    }
}
