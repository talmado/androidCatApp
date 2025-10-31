package com.marc.catappdemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marc.catappdemo.NavDestination
import com.marc.catappdemo.ui.breed.BreedDetailScreen
import com.marc.catappdemo.ui.breed.BreedHomeRoute
import com.marc.catappdemo.ui.favorites.BreedFavoritesRoute
import com.marc.catappdemo.ui.votes.VotesRoute

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController = navController, startDestination = "home") {
        composable(NavDestination.Home.route) {
            BreedHomeRoute(
                modifier,
                onBackClicked = {
                    navController.navigate(it)
                }
            )
        }
        composable("detail/{breedId}") { backStackEntry ->
            val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
            BreedDetailScreen(
                modifier,
                breedId = breedId,
            )
        }
        composable(NavDestination.Favorites.route) {
            BreedFavoritesRoute(modifier)
        }
        composable(NavDestination.Votes.route) {
            VotesRoute(modifier)
        }
    }
}