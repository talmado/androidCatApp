package com.marc.catappdemo.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marc.catappdemo.model.entity.BreedEntity

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, BreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Query("SELECT * FROM breeds WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BreedEntity?

    @Query("DELETE FROM breeds")
    suspend fun clearBreeds()
}