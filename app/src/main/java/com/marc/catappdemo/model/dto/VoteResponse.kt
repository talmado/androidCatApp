package com.marc.catappdemo.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoteResponse(
    val id: Long,
    @SerialName("image_id")
    val imageId: String,
    val value: Int
)