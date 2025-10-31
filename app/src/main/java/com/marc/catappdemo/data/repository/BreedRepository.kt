package com.marc.catappdemo.data.repository

import androidx.paging.PagingData
import com.marc.catappdemo.model.FavoritesState
import com.marc.catappdemo.model.entity.BreedEntity
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    fun breedsStream(): Flow<PagingData<BreedEntity>>
    suspend fun getBreedById(id: String): BreedEntity?
    suspend fun addFavorite(imageId: String, imageUrl: String?)
    suspend fun removeFavorite(imageId: String)
    suspend fun getFavorites(): List<FavoritesState>
    suspend fun addVotes(imageId: String, votes: Int)
}