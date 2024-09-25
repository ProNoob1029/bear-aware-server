package com.dpit

import com.dpit.plugins.configureRouting
import com.dpit.plugins.configureSerialization
import com.dpit.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.flow.MutableStateFlow

val flow = MutableStateFlow(ByteArray(0))

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRouting()
}
