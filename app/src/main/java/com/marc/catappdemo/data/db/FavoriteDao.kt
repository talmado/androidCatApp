package com.marc.catappdemo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marc.catappdemo.model.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE imageId = :imageId")
    suspend fun deleteByImageId(imageId: String)

    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteEntity>
}