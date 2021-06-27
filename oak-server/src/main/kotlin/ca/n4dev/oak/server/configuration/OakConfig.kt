/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.configuration

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.interceptor.PluginInterceptor

class OakConfig(
    val serverName: String,
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val endpoints: List<Endpoint>,
    val preInterceptors: List<Interceptor> = emptyList(),
    val postInterceptors: List<Interceptor> = emptyList()) {
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

    private var preInterceptorsConfiguration: InterceptorConfiguration? = null
    private var postInterceptorsConfiguration: InterceptorConfiguration? = null
    private var endpointConfiguration: EndpointConfiguration? = null
    private var pluginConfiguration: PluginConfiguration? = null

    fun endpoints(builder: EndpointConfiguration.() -> Unit) {
        endpointConfiguration = EndpointConfiguration().apply(builder)
    }

    fun preInterceptors(builder: InterceptorConfiguration.() -> Unit) {
        preInterceptorsConfiguration = InterceptorConfiguration().apply(builder)
    }

    fun postInterceptors(builder: InterceptorConfiguration.() -> Unit) {
        postInterceptorsConfiguration = InterceptorConfiguration().apply(builder)
    }

    fun plugin(builder: PluginConfiguration.() -> Unit) {
        pluginConfiguration = PluginConfiguration().apply(builder)
    }

    fun build(): OakConfig {

        val endpoints = safeList(endpointConfiguration?.endpoints)
        val preInterceptors = safeList(preInterceptorsConfiguration?.interceptors)
        val postInterceptors = safeList(postInterceptorsConfiguration?.interceptors)

        pluginConfiguration?.pluginInterceptors?.forEach {

            when (it) {

                is PluginInterceptor.PreInterceptor -> {
                    preInterceptors.add(it.position, it.interceptor)
                }

                is PluginInterceptor.PostInterceptor -> {
                    postInterceptors.add(it.interceptor)
                }
            }
        }


        return OakConfig(serverName, port, host, endpoints, preInterceptors, postInterceptors)
    }

    private fun <T : Any> initConfig(config: T, init: T.() -> Unit): T {
        config.init()
        return config
    }
}

class InterceptorConfiguration {
    val interceptors = mutableListOf<Interceptor>()

    fun add(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }
}

class EndpointConfiguration {
    val endpoints = mutableListOf<Endpoint>()

    fun add(endpoint: Endpoint) {
        endpoints.add(endpoint)
    }
}

class PluginConfiguration {

    val pluginInterceptors = mutableListOf<PluginInterceptor>()

    fun add(pluginInterceptor: PluginInterceptor) {
        pluginInterceptors.add(pluginInterceptor)
    }
}

private fun <T> safeList(list: MutableList<T>?): MutableList<T> {
    return list ?: mutableListOf()
}