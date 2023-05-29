package com.kevalkanpariya.routes

import com.kevalkanpariya.data.repository.aws.FileStorage
import com.kevalkanpariya.data.requests.EventRequest
import com.kevalkanpariya.data.responses.BasicApiResponse
import com.kevalkanpariya.services.EventService
import com.kevalkanpariya.util.toFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getEvent(eventService: EventService) {

    get("/events") {
        val eventId = call.parameters["id"]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val event = eventService.getEvent(eventId)
        event?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = true,
                    data = event
                )
            )
        }?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                message = "No Such Event is available for given EventId"
            )
        }

    }
}

fun Route.getEvents(eventService: EventService) {

    get("/events") {
        val type = call.parameters["type"]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val pageSize = call.parameters["limit"]?.toInt()?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val page = call.parameters["page"]?.toInt()?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val events = eventService.getEvents(
            type = type,
            pageSize = pageSize,
            page = page
        )

        if (events.isEmpty()) {
            call.respond(
                HttpStatusCode.OK,
                message = "No Such Events are available"
            )
        }

        call.respond(
            status = HttpStatusCode.OK,
            message = BasicApiResponse(
                successful = true,
                data = events
            )
        )

    }
}


fun Route.createEvent(eventService: EventService, fileStorage: FileStorage) {
    post("/events") {

        val multipart = call.receiveMultipart()
        val request = kotlin.runCatching { call.receiveNullable<EventRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val imageUrls: MutableList<String> = mutableListOf()

        multipart.forEachPart { partData ->
            if (partData is PartData.FileItem) {
                imageUrls.add(
                    fileStorage.saveToAWSBucket(
                        file = partData.toFile(),
                        bucketName = "prof-pictures",
                    )
                )
            }

            partData.dispose
        }

        if (imageUrls.isNotEmpty()) {
            val isEventCreated = eventService.createEvent(
                request, imageUrls
            )

            if (!isEventCreated) {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = BasicApiResponse(
                        successful = false,
                        data = null
                    )
                )
                return@post
            }

            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = true,
                    data = null
                )
            )

        }

        call.respond(
            status = HttpStatusCode.Conflict,
            message = BasicApiResponse(
                successful = false,
                data = null,
                message = "ImageUrls are empty"
            )
        )
        return@post

    }
}

fun Route.updateEvent(eventService: EventService, fileStorage: FileStorage) {
    put("/events") {

        val eventId = call.parameters["id"]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }
        val multipart = call.receiveMultipart()
        val request = kotlin.runCatching { call.receiveNullable<EventRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }

        val imageUrls: MutableList<String> = mutableListOf()

        multipart.forEachPart { partData ->
            if (partData is PartData.FileItem) {
                imageUrls.add(
                    fileStorage.saveToAWSBucket(
                        file = partData.toFile(),
                        bucketName = "prof-pictures",
                    )
                )
            }

            partData.dispose
        }

        if (imageUrls.isNotEmpty()) {
            val isEventUpdated = eventService.updateEvent(
                eventId,
                request, imageUrls
            )

            if (!isEventUpdated) {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = BasicApiResponse(
                        successful = false,
                        data = null
                    )
                )
                return@put
            }

            call.respond(
                status = HttpStatusCode.OK,
                message = BasicApiResponse(
                    successful = true,
                    data = null
                )
            )

        }

        call.respond(
            status = HttpStatusCode.Conflict,
            message = BasicApiResponse(
                successful = false,
                data = null,
                message = "ImageUrls are empty"
            )
        )
        return@put

    }
}

fun Route.deleteEvent(
    eventService: EventService
) {

    delete("/events") {
        val eventId = call.parameters["id"]?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        val isEventDeleted = eventService.deleteEvent(eventId)

        if (!isEventDeleted) {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = BasicApiResponse(
                    successful = false,
                    message = "Not Deleted",
                    data = null
                )
            )
            return@delete
        }

        call.respond(
            status = HttpStatusCode.OK,
            message = BasicApiResponse(
                successful = true,
                message = "Event Deleted Sucessfully",
                data = null
            )
        )
    }

}
