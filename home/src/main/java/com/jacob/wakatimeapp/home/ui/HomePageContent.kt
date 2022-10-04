package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.components.cards.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.ui.components.OtherDailyStatsSection
import com.jacob.wakatimeapp.home.ui.components.RecentProjects
import com.jacob.wakatimeapp.home.ui.components.UserDetailsSection
import com.jacob.wakatimeapp.home.ui.components.WeeklyReport
import kotlinx.coroutines.launch

@Composable
fun HomePageContent(
    navigator: HomePageNavigator,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = hiltViewModel(),
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
        when (val viewSateInstance = viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                homePageViewState = viewSateInstance,
                navigator = navigator
            )

            is HomePageViewState.Error -> HomePageError(viewSateInstance)
        }
    }
}

@Composable
private fun HomePageLoaded(
    homePageViewState: HomePageViewState.Loaded,
    navigator: HomePageNavigator,
) {
    val scrollState = rememberScrollState()
    val spacing = MaterialTheme.spacing
    val icons = MaterialTheme.assets.icons
    Column(
        modifier = Modifier
            .padding(horizontal = spacing.medium)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(homePageViewState.userDetails)

        TimeSpentCard(
            gradient = MaterialTheme.gradients.primary,
            roundedCornerPercent = 25,
            iconId = icons.time,
            mainText = "Total Time Spent Today",
            time = homePageViewState.contentData.todaysStats.timeSpent,
            onClick = navigator::toDetailsPage
        )
        Spacer(modifier = Modifier.height(spacing.small))

        RecentProjects(homePageViewState.contentData.todaysStats)
        Spacer(modifier = Modifier.height(spacing.extraSmall))

        WeeklyReport(homePageViewState.contentData.dailyStats)
        Spacer(modifier = Modifier.height(spacing.small))

        OtherDailyStatsSection(
            homePageViewState.contentData.todaysStats,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(spacing.medium))
    }
}

@Composable
private fun HomePageError(errorMessage: HomePageViewState.Error) = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomErrorAnimation,
    text = errorMessage.errorMessage,
    animationTestTag = HomePageTestTags.ERROR_ANIMATION_ILLUSTRATION
)

@Composable
private fun HomePageLoading() = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomLoadingAnimation,
    text = "Loading..",
    animationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION
)
