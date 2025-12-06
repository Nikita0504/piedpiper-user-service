package com.piedpiper

import com.piedpiper.plugins.configureFrameworks
import com.piedpiper.plugins.configureRouting
import com.piedpiper.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureFrameworks()
    configureRouting()
}
