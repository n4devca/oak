/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.interceptor

sealed class PluginInterceptor {
    data class PreInterceptor(val position: Int, val interceptor: Interceptor) : PluginInterceptor()
    data class PostInterceptor(val interceptor: Interceptor) : PluginInterceptor()
}