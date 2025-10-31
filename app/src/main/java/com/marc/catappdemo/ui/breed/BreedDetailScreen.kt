package com.marc.catappdemo.ui.breed

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.marc.catappdemo.R
import com.marc.catappdemo.model.entity.BreedEntity
import org.koin.androidx.compose.koinViewModel


/**
 * A Composable function that displays the detailed information for a specific cat breed.
 *
 * This screen observes the state from the [BreedViewModel] to display the breed's details.
 * It handles loading and error states, showing a progress indicator or a toast message respectively.
 * On success, it displays the breed's image, name, origin, weight, and description using the
 * [BreedDetailScreenContent] composable.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param breedId The unique identifier of the breed to be displayed.
 * @param viewModel The instance of [BreedViewModel] used to fetch breed data and handle business logic.
 *                  It's provided by Koin's `koinViewModel()` by default.
 */
@Composable
fun BreedDetailScreen(
    modifier: Modifier = Modifier,
    breedId: String,
    viewModel: BreedViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val singleBreedState by viewModel.singleBreed.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getSingleBreed(breedId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        when (val state = singleBreedState) {
            is SingleBreedUiState.Loading -> {
                CircularProgressIndicator()
            }

            is SingleBreedUiState.Error -> {
                Toast.makeText(
                    context,
                    stringResource(R.string.no_breed_detail_found), Toast.LENGTH_SHORT
                ).show()
            }

            is SingleBreedUiState.Success -> {
                //val breed = state.breed
                BreedDetailScreenContent(modifier, state.breed, viewModel)
            }
        }
    }
}

@Composable
fun BreedDetailScreenContent(
    modifier: Modifier = Modifier,
    breed: BreedEntity,
    viewModel: BreedViewModel
) {
    var unitMetric by remember { mutableStateOf(true) }
    var countVote by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(300.dp),
                model = breed.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                placeholder = painterResource(R.drawable.black_cat)
            )
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Name: ${breed.name}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Origin: ${breed.origin}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = {
                   /* not yet implemented */
                }
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = "vote"
                )
            }

            Text(
                text = "Votes: $countVote",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        IconButton(
            onClick = {
                viewModel.addFavorite(breed.id, breed.imageUrl)
            }
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "favorite"
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally),
    ) {

        Switch(
            checked = unitMetric,
            onCheckedChange = { unitMetric = it }
        )

        Text(
            text = if (unitMetric) "Weight-metric: ${breed.weightMetric}"
            else "Weight-imperial: ${breed.weightImperial}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    BreedDetailsDescriptionView(breed)
}

@Composable
private fun BreedDetailsDescriptionView(breed: BreedEntity) {
    Column {
        Text(
            text = stringResource(R.string.description),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = breed.description ?: "",
            modifier = Modifier.fillMaxWidth()
        )
    }
}