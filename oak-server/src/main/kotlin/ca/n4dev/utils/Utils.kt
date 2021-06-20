/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.utils

import ca.n4dev.http.HttpMethod
import ca.n4dev.http.HttpRequest
import ca.n4dev.http.HttpResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.OutputStream
import java.nio.charset.Charset

fun copyToStream(data: String, outputStream: OutputStream) {
    data.byteInputStream(Charset.defaultCharset()).copyTo(outputStream)
}

fun toHttpRequest(path: String, request: HttpServletRequest) : HttpRequest {

    val httpRequest = HttpRequest(HttpMethod.valueOf(request.method), path)

    return httpRequest
}

fun toHttpResponse(response: HttpServletResponse): HttpResponse {
    val httpResponse = HttpResponse()

    return httpResponse
}