package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.R.raw
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.components.WtaIllustration
import com.jacob.wakatimeapp.core.ui.components.cards.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.home.R
import com.jacob.wakatimeapp.home.ui.components.*
import com.jacob.wakatimeapp.home.ui.components.RecentProjects
import kotlin.random.Random
import kotlinx.coroutines.launch

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

@Composable
private fun HomePageLoaded(
    homePageViewState: HomePageViewState.Loaded,
    navigator: HomePageNavigator
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(homePageViewState.userDetails)
        Spacer(modifier = Modifier.height(25.dp))

        TimeSpentCard(
            gradient = Gradients.primary,
            roundedCornerPercent = 25,
            iconId = drawable.ic_time,
            mainText = "Total Time Spent Today",
            time = homePageViewState.contentData.todaysStats.timeSpent,
            onClick = navigator::toDetailsPage
        )
        Spacer(modifier = Modifier.height(25.dp))

        RecentProjects(homePageViewState.contentData.todaysStats)
        Spacer(modifier = Modifier.height(10.dp))

        WeeklyReport(homePageViewState.contentData.dailyStats)
        Spacer(modifier = Modifier.height(25.dp))

        OtherDailyStatsSection(
            homePageViewState.contentData.todaysStats,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
private fun HomePageError(errorMessage: HomePageViewState.Error) = WtaAnimation(
    animations = listOf(
        raw.error_1,
        raw.error_2,
        raw.error_animation
    ),
    text = errorMessage.errorMessage,
    animationTestTag = HomePageTestTags.ERROR_ANIMATION_ILLUSTRATION
)

@Composable
private fun HomePageLoading() =
    if (Random.nextBoolean()) WtaAnimation(
        animations = listOf(
            R.raw.loading_1,
            R.raw.loading_2,
            R.raw.loading_animation,
            R.raw.loading_bloob,
            R.raw.loading_paperplane_1,
            R.raw.loading_paperplane_2
        ),
        text = "Loading..",
        animationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION
    ) else WtaIllustration(
        illustrations = listOf(
            R.drawable.il_loading_1,
            R.drawable.il_loading_2,
            R.drawable.il_loading_3,
            R.drawable.il_loading_3a
        ),
        text = "Loading..",
        illustrationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION
    )
