package com.kevalkanpariya.services

import com.kevalkanpariya.data.models.Event
import com.kevalkanpariya.data.repository.event.EventRepository
import com.kevalkanpariya.data.requests.EventRequest


class EventService(
    private val repository: EventRepository
) {

    suspend fun getEvent(eventId: String): Event? {
        return repository.getEvent(eventId)
    }

    suspend fun getEvents(type: String, pageSize: Int, page: Int): List<Event> {
        return repository.getEvents(
            type = type,
            pageSize = pageSize,
            page = page
        )
    }

    suspend fun createEvent(request: EventRequest, imageUrls: List<String>): Boolean {
        return repository.createEvent(request, imageUrls)
    }

    suspend fun updateEvent(eventId: String, event: EventRequest, imageUrls: List<String>): Boolean {
        return repository.updateEvent(
            eventId, event, imageUrls
        )
    }

    suspend fun deleteEvent(eventId: String): Boolean {
        return repository.deleteEvent(eventId)
    }

}