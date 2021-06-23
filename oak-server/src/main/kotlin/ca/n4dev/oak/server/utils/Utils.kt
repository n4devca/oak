/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.server.utils

import ca.n4dev.oak.core.http.Header
import ca.n4dev.oak.core.http.HttpMethod
import ca.n4dev.oak.core.http.HttpRequest
import ca.n4dev.oak.core.http.MultiParameter
import jakarta.servlet.http.HttpServletRequest
import java.io.OutputStream
import java.nio.charset.Charset

fun copyToStream(data: String, outputStream: OutputStream) {
    data.byteInputStream(Charset.defaultCharset()).copyTo(outputStream)
}

fun toHttpRequest(path: String, request: HttpServletRequest) : HttpRequest {

    val parameters: List<MultiParameter> = request.parameterMap.map { p ->
        MultiParameter(p.key, p.value.toList())
    }

    val headers = request.headerNames.toList().map { headerName ->
        request.getHeaders(headerName).toList().map { headerValue ->
            Header(headerName, headerValue)
        }
    }.flatten()

    return HttpRequest(
        HttpMethod.valueOf(request.method),
        path,
        parameters,
        headers,
        request.inputStream)
}
