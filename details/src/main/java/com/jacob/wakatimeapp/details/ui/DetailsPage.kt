package com.jacob.wakatimeapp.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.ui.components.DetailsPageHeader
import com.jacob.wakatimeapp.details.ui.modiffiers.pagerTabIndicatorOffset
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

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

    DetailsPageContent(viewState = viewState, modifier = modifier)
}

@Composable
private fun DetailsPageContent(viewState: DetailsPageViewState, modifier: Modifier = Modifier) =
    Column(
        modifier = modifier.statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (viewState) {
            is DetailsPageViewState.Loading -> DetailsPageLoading()
            is DetailsPageViewState.Loaded -> DetailsPageLoaded(viewState)
            is DetailsPageViewState.Error -> DetailsPageError(viewState)
        }
    }

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DetailsPageLoaded(viewState: DetailsPageViewState.Loaded) {
    val scope = rememberCoroutineScope()
    Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        DetailsPageHeader(
            viewState.projectName,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        )

        val pagerState = rememberPagerState()
        val pages = listOf("Page 1", "Page 2", "Page 3")

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
        ) { page ->
            Text(
                text = "Page $page",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DetailsPageError(errorMessage: DetailsPageViewState.Error) = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomErrorAnimation,
    text = errorMessage.error.message,
)

@Composable
private fun DetailsPageLoading() = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomLoadingAnimation,
    text = "Loading..",
)
