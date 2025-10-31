package com.marc.catappdemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marc.catappdemo.model.entity.BreedEntity
import com.marc.catappdemo.model.entity.FavoriteEntity
import com.marc.catappdemo.model.entity.RemoteKeys


/**
 * The Room database for the application.
 *
 * This database stores information about cat breeds, user favorites, and remote keys for pagination.
 * It serves as the main access point to the persisted data.
 *
 * @property breedDao Provides access to the [BreedEntity] data access object.
 * @property favoriteDao Provides access to the [FavoriteEntity] data access object.
 * @property remoteKeysDao Provides access to the [RemoteKeys] data access object for pagination.
 */
@Database(entities = [BreedEntity::class, FavoriteEntity::class, RemoteKeys::class], version = 1)
abstract class CatDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}