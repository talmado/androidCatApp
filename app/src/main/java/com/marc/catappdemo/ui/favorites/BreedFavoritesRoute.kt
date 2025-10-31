package com.marc.catappdemo.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.marc.catappdemo.R
import com.marc.catappdemo.model.FavoritesState
import com.marc.catappdemo.ui.component.ErrorScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Composable route that displays a list of the user's favorite cat breeds.
 *
 * This function observes the UI state from [BreedFavoritesViewModel] to display different
 * screens for loading, success, and error states. On composition, it triggers
 * the view model to fetch the list of favorites.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The instance of [BreedFavoritesViewModel] used to manage the state and logic
 * for this screen. Injected by Koin by default.
 */
@Composable
fun BreedFavoritesRoute(
    modifier: Modifier = Modifier,
    viewModel: BreedFavoritesViewModel = koinViewModel()
) {

    val favorites by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Column(modifier = modifier.fillMaxSize()) {

        when(val fav = favorites) {
            FavoritesUiState.Error -> ErrorScreen(stringId = R.string.no_favorites)
            FavoritesUiState.Loading -> CircularProgressIndicator()
            is FavoritesUiState.Success -> BreedFavoritesScreen(favorites = fav.favorites)
        }
    }
}

@Composable
fun BreedFavoritesScreen(modifier: Modifier = Modifier, favorites: List<FavoritesState>) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(favorites.size, key = {index -> favorites[index].imageId}) { index ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    model = favorites[index].imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.black_cat)
                )
            }
        }
    }
}