package com.marc.catappdemo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.marc.catappdemo.data.db.CatDatabase
import com.marc.catappdemo.data.service.CatApi
import com.marc.catappdemo.model.FavoritesState
import com.marc.catappdemo.model.entity.BreedEntity
import com.marc.catappdemo.model.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class BreedRepositoryImpl(
    private val api: CatApi,
    private val db: CatDatabase
) : BreedRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun breedsStream(): Flow<PagingData<BreedEntity>> {
        val pagingSourceFactory = { db.breedDao().pagingSource() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = BreedRemoteMediator(api, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getBreedById(id: String): BreedEntity? {
        return db.breedDao().getById(id)
    }

    override suspend fun addFavorite(imageId: String, imageUrl: String?) {
        api.addFavorite(imageId)
        db.favoriteDao().insert(FavoriteEntity(imageId = imageId, imageUrl = imageUrl))
    }

    override suspend fun removeFavorite(imageId: String) {
        db.favoriteDao().deleteByImageId(imageId)
    }

    override suspend fun getFavorites(): List<FavoritesState> {
        return db.favoriteDao().getAll().map {
            FavoritesState(
                imageId = it.imageId,
                imageUrl = it.imageUrl ?: ""
            )
        }
    }

    override suspend fun addVotes(imageId: String, votes: Int) {
        api.postVote(imageId, votes)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}