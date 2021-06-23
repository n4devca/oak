/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.server

import ca.n4dev.oak.server.configuration.OakConfig
import ca.n4dev.oak.server.handler.OakHandler
import ca.n4dev.oak.core.routing.Router
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.util.thread.QueuedThreadPool


object ServerInitializer {

    fun start(oakConfig: OakConfig) {

        // ThreadPool
        val threadPool = QueuedThreadPool()
        threadPool.name = oakConfig.serverName

        val server = Server(threadPool)

        // Connectors
        val connector = getConnector(server, oakConfig)
        server.addConnector(connector)

        // Handlers
        server.handler = getOakHandlers(oakConfig)

        server.start()
    }

    private fun getOakHandlers(oakConfig: OakConfig) : HandlerCollection {
        val handlerCollection = HandlerCollection()

        val router = Router(oakConfig.endpoints.toSet())

        handlerCollection.addHandler(OakHandler(router, oakConfig.preFilters, oakConfig.postFilters))

        return handlerCollection;
    }

    private fun getHttpConfigurationV1(httpConfig: HttpConfiguration, oakConfig: OakConfig): HttpConnectionFactory {
        //...
        return HttpConnectionFactory(httpConfig)
    }

    private fun getHttpConfigurationV2(httpConfig: HttpConfiguration, oakConfig: OakConfig): HTTP2CServerConnectionFactory {
        //...
        return HTTP2CServerConnectionFactory(httpConfig)
    }

    private fun getConnector(server: Server, oakConfig: OakConfig): ServerConnector {

        val httpConfig = HttpConfiguration()
        val connector = ServerConnector(server,
                                        getHttpConfigurationV1(httpConfig, oakConfig),
                                        getHttpConfigurationV2(httpConfig, oakConfig))

        connector.port = oakConfig.port
        connector.host = oakConfig.host
        connector.acceptQueueSize = 10

        return connector
    }

}