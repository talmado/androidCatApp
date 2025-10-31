package com.marc.catappdemo.ui.breed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.marc.catappdemo.R
import org.koin.androidx.compose.koinViewModel

/**
 * A Composable function that represents the main screen for browsing cat breeds.
 * It displays a paginated list of cat breeds in a [LazyColumn]. Each item in the list is
 * a card showing the breed's image and name. Clicking on a card navigates to the
 * detail screen for that breed.
 *
 * This route uses a [BreedViewModel] to fetch the paginated list of breeds.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param onBackClicked A callback function that is invoked when a breed card is clicked.
 *                      It receives a route string (e.g., "detail/{breed.id}") for navigation.
 * @param viewModel The [BreedViewModel] instance used to access breed data, injected by Koin.
 */
@Composable
fun BreedHomeRoute(
    modifier: Modifier = Modifier,
    onBackClicked: (String) -> Unit,
    viewModel: BreedViewModel = koinViewModel()
) {
    val pagingItems = viewModel.breeds.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(pagingItems.itemCount) { breed ->
            val breed = pagingItems[breed]
            breed?.let {

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            onBackClicked("detail/${it.id}")
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth().size(300.dp),
                            model = breed.imageUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = breed.name,
                            placeholder = painterResource(R.drawable.black_cat)
                        )
                        Text(
                            text = breed.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }
        }
    }
}