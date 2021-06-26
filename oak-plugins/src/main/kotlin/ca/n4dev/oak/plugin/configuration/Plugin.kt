/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.plugin.configuration

import ca.n4dev.oak.core.filter.PluginFilter
import ca.n4dev.oak.plugin.filter.BasicAuthenticationFilter
import ca.n4dev.oak.plugin.security.UserAndPasswordValidator
import ca.n4dev.oak.plugin.security.UserService
import ca.n4dev.oak.plugin.security.hash.BcryptPasswordHasher

object Plugin {

    fun basicAuth(userService: UserService): PluginFilter {
        val userAndPasswordValidator = UserAndPasswordValidator(userService, BcryptPasswordHasher())
        return PluginFilter.PreFilter(0, BasicAuthenticationFilter(userAndPasswordValidator))
    }
}
