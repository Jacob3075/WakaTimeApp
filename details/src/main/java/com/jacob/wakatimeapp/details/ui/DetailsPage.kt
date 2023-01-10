package com.jacob.wakatimeapp.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination(navArgsDelegate = DetailsPageNavArgs::class)
fun DetailsPage(
    navigator: DetailsPageNavigator,
    modifier: Modifier = Modifier,
) = DetailsPageScreen(
    navigator = navigator,
    modifier = modifier,
    viewModel = hiltViewModel(),
)

@Composable
@Suppress("UNUSED_PARAMETER")
private fun DetailsPageScreen(
    navigator: DetailsPageNavigator,
    viewModel: DetailsPageViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Details Page",
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}
