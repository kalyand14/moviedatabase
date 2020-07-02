package com.android.omdb.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

fun MockWebServer.enqueueFromFile(fileName: String, headers: Map<String, String> = emptyMap()) {
    val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
    val source = inputStream?.source()?.buffer()
    val mockResponse = MockResponse()
    for ((key, value) in headers) {
        mockResponse.addHeader(key, value)
    }
    if (source != null) {
        enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }
}