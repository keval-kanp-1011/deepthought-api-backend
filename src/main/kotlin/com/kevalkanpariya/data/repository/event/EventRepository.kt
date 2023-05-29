package com.kevalkanpariya.data.repository.event

import com.kevalkanpariya.data.models.Event
import com.kevalkanpariya.data.models.ResponseType
import com.kevalkanpariya.data.requests.EventRequest

interface EventRepository {

    suspend fun getEvent(eventId: String): Event?

    suspend fun getEvents(type: String, pageSize: Int, page: Int): List<Event>

    suspend fun createEvent(request: EventRequest, imageUrls: List<String>): Boolean

    suspend fun updateEvent(eventId: String, request: EventRequest, imageUrls: List<String>): Boolean

    suspend fun deleteEvent(eventId: String): Boolean
}