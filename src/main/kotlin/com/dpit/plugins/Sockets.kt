package com.dpit.plugins

import com.dpit.bearFlow
import com.dpit.cameraFlow
import com.dpit.topic
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
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

                    val message = Message.builder()
                        .putData("title", "new bear")
                        .setTopic(topic)
                        .setAndroidConfig(
                            AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .build()
                        )
                        .build()

                    val response = FirebaseMessaging.getInstance().send(message)

                    println("Successfully sent message: $response")
                }
            }
        }
    }
}
