package com.kevalkanpariya.data.requests

import com.kevalkanpariya.data.models.Category
import com.kevalkanpariya.data.models.User
import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val type: String,
    val uid: Int,
    val name: String,
    val tagline: String,
    val schedule: Long,
    val description: String,
    //val imageUrls: List<String>,
    val moderator: User,
    val category: Category,
    val sub_category: Category,
    val rigor_rank: Int,
    val attendees: Array<Int>
)
