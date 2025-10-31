package com.marc.catappdemo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val id: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val url: String? = null,
)
