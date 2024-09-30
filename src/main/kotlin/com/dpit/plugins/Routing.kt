package com.dpit.plugins

import com.dpit.cameraFlow
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.sample
import kotlinx.io.writeString

const val boundary = "image-boundary"
const val fps = 30

@OptIn(InternalAPI::class, FlowPreview::class)
fun Application.configureRouting() {
    routing {
        get("/camera_output") {
            call.response.header(HttpHeaders.ContentType, "multipart/x-mixed-replace; boundary=$boundary")
            call.respondBytesWriter(contentType = ContentType.MultiPart.Mixed) {
                cameraFlow.sample(1000L / fps).collectLatest { jpegBytes ->
                    writeBuffer.writeString("--$boundary\r\n", Charsets.UTF_8)
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
