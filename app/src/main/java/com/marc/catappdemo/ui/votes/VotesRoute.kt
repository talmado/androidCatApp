package com.marc.catappdemo.ui.votes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.marc.catappdemo.R
import com.marc.catappdemo.ui.component.ErrorScreen

@Composable
fun VotesRoute(modifier: Modifier = Modifier) {
    ErrorScreen(modifier = modifier, stringId = R.string.no_votes_available)
}