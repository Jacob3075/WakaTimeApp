@file:Suppress("MatchingDeclarationName")

package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.home.ui.components.HomePageError
import com.jacob.wakatimeapp.home.ui.components.HomePageLoaded
import com.jacob.wakatimeapp.home.ui.components.HomePageLoading
import kotlinx.coroutines.launch

interface HomePageNavigator {
    fun toDetailsPage()
}

@Composable
fun HomePageContent(
    navigator: HomePageNavigator,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.homePageState.collectAsState()

    LaunchedEffect(viewState) {
        if (viewState !is HomePageViewState.Error) return@LaunchedEffect

        snackBarCoroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = (viewState as HomePageViewState.Error).errorMessage,
                actionLabel = "Action",
                duration = Long
            )
        }
    }

    Column(modifier = modifier.statusBarsPadding()) {
        when (viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                homePageViewState = viewState as HomePageViewState.Loaded,
                navigator = navigator
            )
            is HomePageViewState.Error -> HomePageError(viewState as HomePageViewState.Error)
        }
    }
}
