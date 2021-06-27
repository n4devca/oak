# Oak

![Status](https://github.com/n4devca/oak/actions/workflows/pipeline-maven.yml/badge.svg)

A HIGHLY experimental, MOSTLY non-functional, LARGELY untested web framework inspired by Javalin, Ktor and Spring.

Obligatory hello world:

```kotlin
package test

import ca.n4dev.oak.core.context.HttpContext
import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.interceptor.InterceptorChain
import ca.n4dev.oak.core.interceptor.Interceptor
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.configuration.Plugin
import ca.n4dev.oak.plugin.interceptor.RequestLoggerInterceptor
import ca.n4dev.oak.plugin.security.User
import ca.n4dev.oak.plugin.security.UserService
import ca.n4dev.oak.server.configuration.bootstrap


fun main() {

    val config = bootstrap("test-server") {

        preInterceptors {
            add(RequestLoggerInterceptor())
        }

        endpoints {
            add(helloEndpoint())
        }

        plugin {
            add(Plugin.basicAuth(userService))
        }

    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint() = Endpoint("/hello/{name}") {
    val name = it.httpRequest.pathVariables["name"]
    HttpResponse(Status.OK, ContentType.TEXT, "Hello $name")
}

```

And calling it using httpie: 
```shell script
$ http -a Bob:BobPassword :8080/hello/bob    
                        
HTTP/1.1 200 OK
Content-Length: 9
Content-Type: text/plain
Date: Wed, 23 Jun 2021 11:20:44 GMT

Hello bob
``` 