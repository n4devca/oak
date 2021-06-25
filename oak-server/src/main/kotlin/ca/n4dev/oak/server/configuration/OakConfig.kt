/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.configuration

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.filter.HttpFilter

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

    fun endpoints(builder: EndpointConfiguration.() -> Unit) {
        endpointConfiguration = EndpointConfiguration().apply(builder)
    }

    fun preFilters(builder: HttpFilterConfiguration.() -> Unit) {
        preFilterConfiguration = HttpFilterConfiguration().apply(builder)

    }

    fun postFilters(builder: HttpFilterConfiguration.() -> Unit) {
        postFilterConfiguration = HttpFilterConfiguration().apply(builder)
    }

    fun build(): OakConfig {

        return OakConfig(serverName, port, host,
            safeList(endpointConfiguration?.endpoints),
            safeList(preFilterConfiguration?.filters),
            safeList(postFilterConfiguration?.filters),
        )
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

private fun <T> safeList(list: List<T>?): List<T> {
    return list ?: emptyList()
}