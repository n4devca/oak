/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.routing

import ca.n4dev.oak.core.endpoint.Endpoint
import ca.n4dev.oak.core.http.ContentType
import ca.n4dev.oak.core.http.HttpMethod
import java.net.URLDecoder
import java.util.regex.Pattern

private const val parameterMatcher = "(\\{[a-zA-Z]+\\})"
private const val UTF_8 = "UTF-8"

class RouteMatcher(val endpoint: Endpoint) {

    private val pathVariableNames: List<String>
    private val pathRegEx: String
    private val pathPattern: Pattern

    init {
        pathVariableNames = extractPathVariables(endpoint.path)
        pathRegEx = replaceWithRegEx(endpoint.path, pathVariableNames)
        pathPattern = Pattern.compile(pathRegEx)
    }

    private fun extractPathVariables(path: String) : List<String> {
        val matcher = Pattern.compile(parameterMatcher).matcher(path)
        val variables = mutableListOf<String>()
        while (matcher.find()) {
            variables.add(matcher.group(1).removePrefix("{").removeSuffix("}"))
        }

        return variables
    }

    private fun replaceWithRegEx(path: String, pathVariables: List<String>): String {
        var transformedPath = path
        pathVariables.forEach {
            transformedPath = transformedPath.replace("{${it}}", "([^/]+)")
        }

        return transformedPath
    }


    fun match(requestedPath: String,
              httpMethod: HttpMethod = HttpMethod.GET,
              contentType: ContentType = ContentType.ALL) : Route? {

        val matcher = pathPattern.matcher(requestedPath)

        val handlingMethodMatched = isHandlingMethod(httpMethod, endpoint)
        val producingContentTypeMatched = isProducingContentType(contentType, endpoint)


        val matched = handlingMethodMatched && producingContentTypeMatched &&
                                matcher.matches() &&
                                matcher.groupCount() == pathVariableNames.size


        return if (matched) {

            val pathVariables = pathVariableNames.mapIndexed { index, pathVariable ->
                // Start at 1 since matcher[0] is the string, not the first group
                pathVariable to URLDecoder.decode(matcher.group(index + 1), UTF_8)
            }.toMap()

            Route(endpoint, pathVariables)

        } else {
            null
        }

    }

    private fun isHandlingMethod(httpMethod: HttpMethod, endpoint: Endpoint): Boolean {
        return httpMethod == HttpMethod.ANY || httpMethod == endpoint.method || endpoint.method == HttpMethod.ANY
    }

    private fun isProducingContentType(contentType: ContentType, endpoint: Endpoint): Boolean {
        return contentType == endpoint.producing || endpoint.producing == ContentType.ALL
    }
}

class Route (val endpoint: Endpoint, val pathVariables: Map<String, String>)


