package com.piedpiper.plugins

import com.piedpiper.features.user.userRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/PiedPiper/api/v1") {
            userRoute()
        }
    }
}
