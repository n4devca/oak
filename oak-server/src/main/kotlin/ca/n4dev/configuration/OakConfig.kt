/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.configuration

import ca.n4dev.endpoint.Endpoint
import ca.n4dev.filter.Filter

class OakConfig(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val filters: List<Filter>,
    val endpoints: List<Endpoint>) {
}

fun bootstrap(serverName: String,
              port: Int = 8080,
              host: String = "127.0.0.1",
              builder: OakConfigBuilder.() -> Unit): OakConfigBuilder {
    return OakConfigBuilder(serverName, port, host).apply(builder)
}

class OakConfigBuilder(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1") {

    private var filterConfiguration: FilterConfiguration? = null
    private var endpointConfiguration: EndpointConfiguration? = null

//    fun activate(modules: List<Any>) {
//        TODO("not implemented yet")
//    }

    fun filters(builder: FilterConfiguration.() -> Unit) {
        filterConfiguration = FilterConfiguration().apply(builder)
    }

    fun endpoints(builder: EndpointConfiguration.() -> Unit) {
        endpointConfiguration = EndpointConfiguration().apply(builder)
    }

    fun build(): OakConfig {

        return OakConfig(serverName, port, host,
            safeList(filterConfiguration?.filters),
            safeList(endpointConfiguration?.endpoints))
    }
}

class FilterConfiguration {
    val filters = mutableListOf<Filter>()
}

class EndpointConfiguration {
    val endpoints = mutableListOf<Endpoint>()
}

private fun <T> safeList(list: List<T>?): List<T> {
    return list ?: emptyList()
}