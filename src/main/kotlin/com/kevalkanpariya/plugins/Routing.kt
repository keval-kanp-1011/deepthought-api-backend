package com.kevalkanpariya.plugins

import com.kevalkanpariya.data.repository.aws.FileStorage
import com.kevalkanpariya.routes.*
import com.kevalkanpariya.services.EventService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {

        val fileStorage by inject<FileStorage>()
        /*Services*/
        val eventService by inject<EventService>()

        get("/") {
            call.respondText("Welcome to Event Service API!")
        }

        getEvent(eventService)
        getEvents(eventService)
        createEvent(eventService, fileStorage)
        updateEvent(eventService, fileStorage)
        deleteEvent(eventService)
    }
}
