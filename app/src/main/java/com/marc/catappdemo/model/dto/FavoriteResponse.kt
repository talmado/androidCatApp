package com.marc.catappdemo.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    val id: Long,
    val message: String,
    @SerialName("image_id")
    val imageId: String,
)
