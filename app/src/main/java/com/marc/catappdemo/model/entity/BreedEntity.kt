package com.marc.catappdemo.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val origin: String?,
    val weightMetric: String?,
    val weightImperial: String?,
    val imageUrl: String?
)
