package com.dpit.plugins

import com.dpit.cameraFlow
import com.dpit.detectionChannel
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/camera_input") { // websocketSession
            for (frame in incoming) {
                if (frame is Frame.Binary) {
                    val data = frame.readBytes()
                    cameraFlow.value = data
                }
                if (frame is Frame.Text) {
                    println(frame.readText())


                    launch {
                        detectionChannel.send(Unit)
                    }
                }
            }
        }
    }
}
