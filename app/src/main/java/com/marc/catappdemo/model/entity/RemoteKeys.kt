package com.marc.catappdemo.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val breedId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
