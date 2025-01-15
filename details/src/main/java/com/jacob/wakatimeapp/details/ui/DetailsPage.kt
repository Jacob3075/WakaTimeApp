package com.jacob.wakatimeapp.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.ui.components.DetailsPageHeader
import com.jacob.wakatimeapp.details.ui.components.Tabs
import com.jacob.wakatimeapp.details.ui.components.TimeTab
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
@Destination(navArgsDelegate = DetailsPageNavArgs::class)
fun DetailsPage(
    navigator: DetailsPageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) = DetailsPageScreen(
    navigator = navigator,
    modifier = modifier,
    snackbarHostState = snackbarHostState,
    viewModel = hiltViewModel(),
)

@Composable
@Suppress("UNUSED_PARAMETER")
private fun DetailsPageScreen(
    navigator: DetailsPageNavigator,
    viewModel: DetailsPageViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.viewState.collectAsState()
    val today = viewModel.getTodaysDate()

    LaunchedEffect(viewState) {
        if (viewState !is DetailsPageViewState.Error) return@LaunchedEffect
        val viewStateError = viewState as DetailsPageViewState.Error

        snackBarCoroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = viewStateError.error.message,
                duration = Long,
            )
        }
    }

    Column(
        modifier = modifier.statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (val state = viewState) {
            is DetailsPageViewState.Loading -> DetailsPageLoading()
            is DetailsPageViewState.Loaded -> DetailsPageLoaded(state, today)
            is DetailsPageViewState.Error -> DetailsPageError(state)
        }
    }
}

@Composable
private fun DetailsPageLoaded(viewState: DetailsPageViewState.Loaded, today: LocalDate) {
    listOf(
        Tabs.Time,
        Tabs.Languages,
        Tabs.Editors,
        Tabs.OperatingSystems,
    ).toImmutableList()

    Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        DetailsPageHeader(
            viewState.projectName,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        )

        TimeTab(detailsPageData = viewState, today)
    }
}

@Composable
private fun DetailsPageError(errorMessage: DetailsPageViewState.Error) = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomErrorAnimation,
    text = errorMessage.error.message,
    speed = 1f,
)

@Composable
private fun DetailsPageLoading() = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomLoadingAnimation,
    text = "Loading..",
    speed = 1f,
)
