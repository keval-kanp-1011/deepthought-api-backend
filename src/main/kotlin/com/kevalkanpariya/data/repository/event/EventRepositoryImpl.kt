package com.kevalkanpariya.data.repository.event

import com.kevalkanpariya.data.models.Event
import com.kevalkanpariya.data.models.ResponseType
import com.kevalkanpariya.data.models.User
import com.kevalkanpariya.data.requests.EventRequest
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class EventRepositoryImpl(
    db: CoroutineDatabase
) : EventRepository {

    private val users = db.getCollection<User>()
    private val events = db.getCollection<Event>()


    override suspend fun getEvent(eventId: String): Event? {
        return events.findOne(filter = Event::eventId eq eventId)
    }

    override suspend fun getEvents(type: String, pageSize: Int, page: Int): List<Event> {
        return events
            .find(filter = Event::type eq type)
            .skip(page * pageSize)
            .limit(pageSize)
            .toList()
    }

    override suspend fun createEvent(request: EventRequest, imageUrls: List<String>): Boolean {
        return events.insertOne(
            Event(
                type = request.type,
                uid = request.uid,
                name = request.name,
                tagline = request.tagline,
                schedule = request.schedule,
                description = request.description,
                imageUrls = imageUrls,
                moderator = request.moderator,
                category = request.category,
                sub_category = request.sub_category,
                rigor_rank = request.rigor_rank,
                attendees = request.attendees,
            )
        ).wasAcknowledged()
    }

    override suspend fun updateEvent(eventId: String, request: EventRequest, imageUrls: List<String>): Boolean {
        return events.updateOne(
            filter = Event::eventId eq eventId,
            update = combine(
                setValue(Event::attendees, request.attendees),
                setValue(Event::category, request.category),
                setValue(Event::description, request.description),
                setValue(Event::imageUrls, imageUrls),
                setValue(Event::moderator, request.moderator),
                setValue(Event::name, request.name),
                setValue(Event::rigor_rank, request.rigor_rank),
                setValue(Event::schedule, request.schedule),
                setValue(Event::sub_category, request.sub_category),
                setValue(Event::tagline, request.tagline),
                setValue(Event::type, request.type),
                setValue(Event::uid, request.uid)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteEvent(eventId: String): Boolean {
        return events.deleteOne(filter = Event::eventId eq eventId).wasAcknowledged()
    }
}