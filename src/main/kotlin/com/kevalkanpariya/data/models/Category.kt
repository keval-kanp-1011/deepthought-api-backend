package com.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val name: String,
    val catId: String
)