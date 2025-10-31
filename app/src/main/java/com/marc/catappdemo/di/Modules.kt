package com.marc.catappdemo.di

import android.app.Application
import androidx.room.Room
import com.marc.catappdemo.BuildConfig
import com.marc.catappdemo.data.db.CatDatabase
import com.marc.catappdemo.data.repository.BreedRepository
import com.marc.catappdemo.data.repository.BreedRepositoryImpl
import com.marc.catappdemo.data.service.CatApi
import com.marc.catappdemo.ui.breed.BreedViewModel
import com.marc.catappdemo.ui.favorites.BreedFavoritesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(OkHttp) {

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.thecatapi.com/v1"
                    parameters.append("api_key", BuildConfig.API_KEY)
                }
            }

            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) {
                logger = Logger.SIMPLE
            }
        }
    }

    single { Dispatchers.IO }

    single { CatApi(get(), get()) }

    single {
        Room.databaseBuilder(
            get<Application>().applicationContext,
            CatDatabase::class.java,
            "cat-db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<CatDatabase>().breedDao() }

    single<BreedRepository> { BreedRepositoryImpl(get(), get()) }

    viewModel { BreedViewModel(get()) }

    viewModel { BreedFavoritesViewModel(get()) }
}