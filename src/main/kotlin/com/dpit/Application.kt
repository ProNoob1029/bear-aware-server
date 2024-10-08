package com.dpit

import com.dpit.plugins.configureRouting
import com.dpit.plugins.configureSerialization
import com.dpit.plugins.configureSockets
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileInputStream
import java.util.logging.LogManager

val cameraFlow = MutableStateFlow(ByteArray(0))
val bearFlow = MutableStateFlow("")

const val serviceAccountFilepath = "/home/dragos/IdeaProjects/bear-aware-server/bear-aware-notifications-firebase-adminsdk-9zb80-9f6b349760.json"

const val topic = "bear-alert"

fun main() {
    val serviceAccount = FileInputStream(serviceAccountFilepath)
    val firebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(firebaseOptions)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRouting()
}
