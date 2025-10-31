package com.marc.catappdemo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.marc.catappdemo.data.db.CatDatabase
import com.marc.catappdemo.data.service.CatApi
import com.marc.catappdemo.model.entity.BreedEntity

@OptIn(ExperimentalPagingApi::class)
class BreedRemoteMediator(
    private val api: CatApi,
    private val db: CatDatabase
) : RemoteMediator<Int, BreedEntity>()  {

    private val breedDao = db.breedDao()
    private var entities = emptyList<BreedEntity>()


    override suspend fun load(loadType: LoadType, state: PagingState<Int, BreedEntity>): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastPage = (state.pages.size)
                    lastPage
                }
            }

            val limit = state.config.pageSize
            api.getBreeds(page = page, limit = limit).onSuccess { breedDto ->
                entities = breedDto.map { dto ->
                    BreedEntity(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        origin = dto.origin,
                        weightMetric = dto.weight?.metric,
                        weightImperial = dto.weight?.imperial,
                        imageUrl = dto.image?.url
                    )
                }
            }.onFailure {
                MediatorResult.Error(it)
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    breedDao.clearBreeds()
                }
                breedDao.insertAll(entities)
            }

            return MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}