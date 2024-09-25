package com.dpit.plugins

import com.dpit.flow
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.io.writeString

@OptIn(InternalAPI::class)
fun Application.configureRouting() {

    routing {
        get("/") {
            call.response.header(HttpHeaders.ContentType, "multipart/x-mixed-replace; boundary=image-boundary")
            call.respondBytesWriter(contentType = ContentType.MultiPart.Mixed) {
                flow.collect { jpegBytes ->
                    writeBuffer.writeString("--image-boundary\r\n", Charsets.UTF_8)
                    writeBuffer.writeString("Content-Type: image/jpeg\r\n", Charsets.UTF_8)
                    writeBuffer.writeString("Content-Length: ${jpegBytes.size}\r\n\r\n", Charsets.UTF_8)

                    writeBuffer.writeFully(jpegBytes)

                    writeBuffer.writeString("\r\n\r\n", Charsets.UTF_8)

                    flush()
                }
            }
        }
    }
}
