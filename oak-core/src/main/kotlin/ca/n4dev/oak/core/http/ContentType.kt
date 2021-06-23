/**
 * Copyright 2021 Remi Guillemette
 * SPDX-License-Identifier: Apache-2.0
 */
package ca.n4dev.oak.core.http

enum class ContentType(val value: String) {
    JSON("application/json"),
    TEXT("text/plain"),
    HTML("text/html"),
    FORM_DATA("application/ x-www-form-urlencoded"),
    MULTI_PART("multipart/form-data"),
    ALL("*/*")
    ;
}