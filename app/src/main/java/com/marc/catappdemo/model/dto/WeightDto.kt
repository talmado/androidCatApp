package com.marc.catappdemo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeightDto(
    val imperial: String,
    val metric: String
)
