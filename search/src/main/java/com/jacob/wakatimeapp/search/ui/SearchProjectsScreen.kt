package com.jacob.wakatimeapp.search.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Error
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loaded
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loading

@Composable
fun SearchProjectsScreen(
    navigator: SearchProjectsNavigator,
    modifier: Modifier = Modifier,
) = SearchProjectsScreen(
    navigator = navigator,
    modifier = modifier,
    viewModel = hiltViewModel(),
)

@Composable
private fun SearchProjectsScreen(
    navigator: SearchProjectsNavigator,
    viewModel: SearchProjectsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.searchProjectsState.collectAsState()

    when (state) {
        is Loaded -> TODO()
        Loading -> WtaAnimation(
            animation = MaterialTheme.assets.animations.randomLoadingAnimation,
            text = "Loading..",
        )

        is Error -> WtaAnimation(
            animation = MaterialTheme.assets.animations.randomErrorAnimation,
            text = (state as Error).error.message,
        )
    }
}
