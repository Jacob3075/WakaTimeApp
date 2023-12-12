package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Project
import com.jacob.wakatimeapp.core.ui.WtaDevicePreviews
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.components.cards.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.domain.models.HomePageUserDetails
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.ui.components.OtherDailyStatsSection
import com.jacob.wakatimeapp.home.ui.components.RecentProjects
import com.jacob.wakatimeapp.home.ui.components.UserDetailsSection
import com.jacob.wakatimeapp.home.ui.components.WeeklyReport
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import timber.log.Timber

@Composable
@Destination
fun HomePage(
    navigator: HomePageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) = HomePageScreen(navigator, snackbarHostState, modifier, hiltViewModel())

@Composable
private fun HomePageScreen(
    navigator: HomePageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = hiltViewModel(),
) {
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.homePageState.collectAsState()
    val todaysDate = remember { viewModel.getTodaysDate() }

    LaunchedEffect(viewState) {
        Timber.d("viewState: $viewState")
        if (viewState !is HomePageViewState.Error) return@LaunchedEffect
        val viewStateError = viewState as HomePageViewState.Error

        snackBarCoroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = viewStateError.error.message,
                duration = SnackbarDuration.Long,
            )
        }
    }

    HomePageContent(
        viewState = viewState,
        toDetailsPage = navigator::toProjectDetailsPage,
        toSearchPage = navigator::toSearchPage,
        todaysDate = todaysDate,
        modifier = modifier,
    )
}

@Composable
private fun HomePageContent(
    viewState: HomePageViewState,
    toDetailsPage: (String) -> Unit,
    toSearchPage: () -> Unit,
    modifier: Modifier = Modifier,
    todaysDate: LocalDate,
) {
    Column(modifier = modifier.statusBarsPadding()) {
        when (viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                homePageViewState = viewState,
                toDetailsPage = toDetailsPage,
                toSearchPage = toSearchPage,
                todaysDate = todaysDate,
            )

            is HomePageViewState.Error -> HomePageError(viewState)
        }
    }
}

@Composable
private fun HomePageLoaded(
    homePageViewState: HomePageViewState.Loaded,
    toDetailsPage: (String) -> Unit,
    toSearchPage: () -> Unit = {},
    todaysDate: LocalDate,
) {
    val scrollState = rememberScrollState()
    val spacing = MaterialTheme.spacing
    val icons = MaterialTheme.assets.icons
    Column(
        modifier = Modifier
            .padding(horizontal = spacing.medium)
            .verticalScroll(scrollState),
    ) {
        UserDetailsSection(
            fullName = homePageViewState.userDetails.fullName,
            photoUrl = homePageViewState.userDetails.photoUrl,
        )

        TimeSpentCard(
            statsType = "Total Time Spent Today",
            time = homePageViewState.last7DaysStats.timeSpentToday,
            gradient = MaterialTheme.gradients.facebookMessenger,
            iconId = icons.time,
            roundedCornerPercent = 25,
            onClick = {},
        )
        Spacer(modifier = Modifier.height(spacing.small))

        RecentProjects(
            homePageViewState.last7DaysStats.projectsWorkedOn,
            onSeeAllClicked = toSearchPage,
            onProjectClicked = toDetailsPage,
        )
        Spacer(modifier = Modifier.height(spacing.extraSmall))

        WeeklyReport(homePageViewState.last7DaysStats.weeklyTimeSpent, today = todaysDate)
        Spacer(modifier = Modifier.height(spacing.small))

        OtherDailyStatsSection(
            onClick = {},
            mostUsedLanguage = homePageViewState.last7DaysStats.mostUsedLanguage,
            mostUsedOs = homePageViewState.last7DaysStats.mostUsedOs,
            mostUsedEditor = homePageViewState.last7DaysStats.mostUsedEditor,
            currentStreak = homePageViewState.currentStreak,
            longestStreak = homePageViewState.longestStreak,
            numberOfDaysWorked = homePageViewState.last7DaysStats.numberOfDaysWorked,
        )
        Spacer(modifier = Modifier.height(spacing.medium))
    }
}

@Composable
private fun HomePageError(errorMessage: HomePageViewState.Error) = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomErrorAnimation,
    text = errorMessage.error.message,
    modifier = Modifier.fillMaxSize(),
)

@Composable
private fun HomePageLoading() = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomLoadingAnimation,
    text = "Loading..",
    modifier = Modifier.fillMaxSize(),
)

@WtaDevicePreviews
@Composable
private fun HomePagePreview(
    @PreviewParameter(HomePagePreviewProvider::class) viewState: HomePageViewState,
) = WakaTimeAppTheme {
    HomePageContent(viewState = viewState, toDetailsPage = { }, toSearchPage = { }, todaysDate = LocalDate(2023, 1, 1))
}

private class HomePagePreviewProvider : CollectionPreviewParameterProvider<HomePageViewState>(
    listOf(
        HomePageViewState.Loading,
        HomePageViewState.Error(Error.UnknownError("Something went wrong")),
        HomePageViewState.Loaded(
            last7DaysStats = Last7DaysStats(
                timeSpentToday = Time.ZERO,
                projectsWorkedOn = listOf<Project>().toImmutableList(),
                weeklyTimeSpent = mapOf<LocalDate, Time>().toImmutableMap(),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = "",
            ),
            userDetails = HomePageUserDetails(
                photoUrl = "",
                fullName = "",
            ),
            currentStreak = Streak.ZERO,
            longestStreak = Streak.ZERO,
        ),
    ),
)
