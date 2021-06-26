/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.configuration

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.filter.HttpFilter
import ca.n4dev.oak.core.filter.PluginFilter

class OakConfig(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val endpoints: List<Endpoint>,
    val preFilters: List<HttpFilter> = emptyList(),
    val postFilters: List<HttpFilter> = emptyList()) {
}

fun bootstrap(serverName: String,
              port: Int = 8080,
              host: String = "127.0.0.1",
              init: OakConfigBuilder.() -> Unit): OakConfigBuilder {

    val builder = OakConfigBuilder(serverName, port, host)

    builder.init()

    return builder
}

class OakConfigBuilder(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1") {

    private var preFilterConfiguration: HttpFilterConfiguration? = null
    private var postFilterConfiguration: HttpFilterConfiguration? = null
    private var endpointConfiguration: EndpointConfiguration? = null
    private var pluginConfiguration: PluginConfiguration? = null

    fun endpoints(builder: EndpointConfiguration.() -> Unit) {
        endpointConfiguration = EndpointConfiguration().apply(builder)
    }

    fun preFilters(builder: HttpFilterConfiguration.() -> Unit) {
        preFilterConfiguration = HttpFilterConfiguration().apply(builder)
    }

    fun postFilters(builder: HttpFilterConfiguration.() -> Unit) {
        postFilterConfiguration = HttpFilterConfiguration().apply(builder)
    }

    fun plugin(builder: PluginConfiguration.() -> Unit) {
        pluginConfiguration = PluginConfiguration().apply(builder)
    }

    fun build(): OakConfig {

        val endpoints = safeList(endpointConfiguration?.endpoints)
        val preFilters = safeList(preFilterConfiguration?.filters)
        val postfilters = safeList(postFilterConfiguration?.filters)

        pluginConfiguration?.pluginFilters?.forEach {

            when (it) {

                is PluginFilter.PreFilter -> {
                    preFilters.add(it.position, it.filter)
                }

                is PluginFilter.PostFilter -> {
                    postfilters.add(it.filter)
                }
            }
        }


        return OakConfig(serverName, port, host, endpoints, preFilters, postfilters)
    }

    private fun <T : Any> initConfig(config: T, init: T.() -> Unit): T {
        config.init()
        return config
    }
}

class HttpFilterConfiguration {
    val filters = mutableListOf<HttpFilter>()

    fun add(filter: HttpFilter) {
        filters.add(filter)
    }
}

class EndpointConfiguration {
    val endpoints = mutableListOf<Endpoint>()

    fun add(endpoint: Endpoint) {
        endpoints.add(endpoint)
    }
}

class PluginConfiguration {

    val pluginFilters = mutableListOf<PluginFilter>()

    fun add(pluginFilter: PluginFilter) {
        pluginFilters.add(pluginFilter)
    }
}

private fun <T> safeList(list: MutableList<T>?): MutableList<T> {
    return list ?: mutableListOf()
}