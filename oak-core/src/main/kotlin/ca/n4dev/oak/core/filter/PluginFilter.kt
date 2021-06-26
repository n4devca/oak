/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.filter

sealed class PluginFilter {
    data class PreFilter(val position: Int, val filter: HttpFilter) : PluginFilter()
    data class PostFilter(val filter: HttpFilter) : PluginFilter()
}