package com.marc.catappdemo.data.service

import com.marc.catappdemo.model.dto.BreedDto
import com.marc.catappdemo.model.dto.FavoriteResponse
import com.marc.catappdemo.model.dto.VoteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


/**
 * A client for interacting with TheCatAPI.
 * This class encapsulates all the network calls to the cat API, handling requests for breeds,
 * voting, and managing favorites. It uses Ktor for HTTP requests and wraps responses
 * in an [ApiOperation] sealed interface to handle success and failure states gracefully.
 *
 * @param client The [HttpClient] instance used for making network requests.
 * @param dispatcher The [CoroutineDispatcher] to be used for executing the API calls on a background thread.
 */
class CatApi(private val client: HttpClient, private val dispatcher: CoroutineDispatcher) {

    suspend fun getBreeds(page: Int, limit: Int): ApiOperation<List<BreedDto>> =
        withContext(dispatcher) {
            return@withContext safeApiCall {
                client.get("breeds") {
                    parameter("page", page)
                    parameter("limit", limit)
                }.body()
            }
        }

    suspend fun postVote(imageId: String, value: Int): ApiOperation<VoteResponse> {
        return safeApiCall {
            client.post("votes") {
                setBody(mapOf("image_id" to imageId, "value" to value))
            }.body()
        }
    }

    suspend fun addFavorite(imageId: String): ApiOperation<FavoriteResponse> {
        return safeApiCall {
            client.post("favourites") {
                setBody(mapOf("image_id" to imageId))
            }.body()
        }
    }

    suspend fun deleteFavorite(favoriteId: String) {
        safeApiCall {
            client.delete("favourites/$favoriteId")
        }
    }
}

private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
    return try {
        ApiOperation.Success(data = apiCall())
    } catch (e: Exception) {
        ApiOperation.Failure(exception = e)
    }
}

sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    suspend fun onSuccess(block: suspend (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}