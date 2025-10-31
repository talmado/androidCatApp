package com.marc.catappdemo.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marc.catappdemo.data.repository.BreedRepository
import com.marc.catappdemo.model.FavoritesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BreedFavoritesViewModel(private val repository: BreedRepository): ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun getFavorites() = viewModelScope.launch {
        _uiState.value = repository.getFavorites().mapToUiState()
    }
}

sealed interface FavoritesUiState {
    object Loading: FavoritesUiState
    object Error: FavoritesUiState
    data class Success(val favorites: List<FavoritesState>): FavoritesUiState
}

fun List<FavoritesState>.mapToUiState(): FavoritesUiState {
    return if (this.isNotEmpty()) {
        FavoritesUiState.Success(this)
    } else {
        FavoritesUiState.Error
    }
}