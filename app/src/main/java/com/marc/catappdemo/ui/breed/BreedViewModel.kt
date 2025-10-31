package com.marc.catappdemo.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marc.catappdemo.data.repository.BreedRepository
import com.marc.catappdemo.model.entity.BreedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BreedViewModel(private val repository: BreedRepository) : ViewModel() {
    val breeds: Flow<PagingData<BreedEntity>> = repository.breedsStream().cachedIn(viewModelScope)

    private val _singleBreed = MutableStateFlow<SingleBreedUiState>(SingleBreedUiState.Loading)
    val singleBreed: StateFlow<SingleBreedUiState> = _singleBreed.asStateFlow()


    fun getSingleBreed(id: String) = viewModelScope.launch {
        println("breed id: $id")
        _singleBreed.value = repository.getBreedById(id).mapToSingleBreedUiState()
    }

    fun addFavorite(imageId: String, imageUrl: String?) =
        viewModelScope.launch { repository.addFavorite(imageId, imageUrl) }

    fun removeFavorite(imageId: String) =
        viewModelScope.launch { repository.removeFavorite(imageId) }

    fun addVote(imageId: String, vote: Int) = viewModelScope.launch {
        repository.addVotes(imageId, vote)
    }

}

sealed interface SingleBreedUiState {
    data class Success(val breed: BreedEntity) : SingleBreedUiState
    object Error : SingleBreedUiState
    object Loading : SingleBreedUiState
}

fun BreedEntity?.mapToSingleBreedUiState(): SingleBreedUiState {
    return this?.let {
        SingleBreedUiState.Success(breed = it)
    } ?: SingleBreedUiState.Error
}