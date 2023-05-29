package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val eventId: String = "",
    val type: String,
    val uid: Int,
    val name: String,
    val tagline: String,
    val schedule: Long,
    val description: String,
    val imageUrls: List<String>,
    val moderator: User,
    val category: Category,
    val sub_category: Category,
    val rigor_rank: Int,
    val attendees: Array<Int>
)



enum class ResponseType {
    EVENT
}

