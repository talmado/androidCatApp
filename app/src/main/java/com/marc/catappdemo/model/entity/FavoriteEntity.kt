package com.marc.catappdemo.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val imageId: String,
    val imageUrl: String?
)
