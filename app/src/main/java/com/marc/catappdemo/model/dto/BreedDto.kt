package com.marc.catappdemo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class BreedDto(
    val id: String,
    val name: String,
    val temperament: String? = null,
    val origin: String? = null,
    val weight: WeightDto? = null,
    val description: String? = null,
    val life_span: String? = null,
    val image: ImageDto? = null
)
