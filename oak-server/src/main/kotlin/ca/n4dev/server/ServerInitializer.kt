/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.server

import ca.n4dev.configuration.OakConfig
import ca.n4dev.handler.OakHandler
import ca.n4dev.http.RequestLogger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.AbstractHandler
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
        server.handler = getHandlers()

        server.start()
    }

    private fun getHandlers() : HandlerCollection {
        val handlerCollection = HandlerCollection()

        handlerCollection.addHandler(OakHandler(RequestLogger()))
        handlerCollection.addHandler(object: AbstractHandler() {
            override fun handle(p0: String, p1: Request, p2: HttpServletRequest, p3: HttpServletResponse) {
                p3.sendError(404)
            }

        })

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