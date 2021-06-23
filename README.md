# Oak

A HIGHLY experimental, MOSTLY non-functional, LARGELY untested web framework inspired by Javalin, Ktor and Spring.

Obligatory hello world:

```kotlin
package test

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpResponse
import ca.n4dev.oak.core.http.Status
import ca.n4dev.oak.plugin.filter.RequestLoggerFilter
import ca.n4dev.oak.server.configuration.bootstrap


fun main() {

    val config = bootstrap("test-server") {

        preFilters {
            add(RequestLoggerFilter())
        }

        endpoints {
            add(helloEndpoint())
        }

    }.build()

    ServerInitializer.start(config)
}


private fun helloEndpoint() = Endpoint("/hello/{name}") {
    val name = it.pathVariables["name"]
    HttpResponse(Status.OK, ContentType.TEXT, "Hello $name")
}
```

And calling it using httpie: 
```shell script
$ http :8080/hello/bob    
                        
HTTP/1.1 200 OK
Content-Length: 9
Content-Type: text/plain
Date: Wed, 23 Jun 2021 11:20:44 GMT

Hello bob
``` 