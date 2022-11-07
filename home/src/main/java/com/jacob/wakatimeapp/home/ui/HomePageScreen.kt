package com.jacob.wakatimeapp.home.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.components.cards.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.domain.models.HomePageUserDetails
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import com.jacob.wakatimeapp.home.domain.models.Streaks
import com.jacob.wakatimeapp.home.ui.components.OtherDailyStatsSection
import com.jacob.wakatimeapp.home.ui.components.RecentProjects
import com.jacob.wakatimeapp.home.ui.components.UserDetailsSection
import com.jacob.wakatimeapp.home.ui.components.WeeklyReport
import kotlinx.coroutines.launch

@Composable
fun HomePageScreen(
    navigator: HomePageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = hiltViewModel(),
) {
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.homePageState.collectAsState()

    LaunchedEffect(viewState) {
        if (viewState !is HomePageViewState.Error) return@LaunchedEffect
        val viewStateError = viewState as HomePageViewState.Error

        snackBarCoroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = viewStateError.error.message,
                duration = SnackbarDuration.Long
            )
        }
    }

    HomePageContent(
        viewState = viewState,
        toDetailsPage = navigator::toDetailsPage,
        modifier = modifier
    )
}

@Composable
private fun HomePageContent(
    viewState: HomePageViewState,
    toDetailsPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.statusBarsPadding()) {
        when (viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                homePageViewState = viewState,
                toDetailsPage = toDetailsPage,
            )

            is HomePageViewState.Error -> HomePageError(viewState)
        }
    }
}

@Composable
private fun HomePageLoaded(
    homePageViewState: HomePageViewState.Loaded,
    toDetailsPage: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val spacing = MaterialTheme.spacing
    val icons = MaterialTheme.assets.icons
    Column(
        modifier = Modifier
            .padding(horizontal = spacing.medium)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(
            fullName = homePageViewState.userDetails.fullName,
            photoUrl = homePageViewState.userDetails.photoUrl
        )

        TimeSpentCard(
            gradient = MaterialTheme.gradients.facebookMessenger,
            roundedCornerPercent = 25,
            iconId = icons.time,
            mainText = "Total Time Spent Today",
            time = homePageViewState.last7DaysStats.timeSpentToday,
            onClick = toDetailsPage
        )
        Spacer(modifier = Modifier.height(spacing.small))

        RecentProjects(homePageViewState.last7DaysStats.projectsWorkedOn)
        Spacer(modifier = Modifier.height(spacing.extraSmall))

        WeeklyReport(homePageViewState.last7DaysStats.weeklyTimeSpent)
        Spacer(modifier = Modifier.height(spacing.small))

        OtherDailyStatsSection(
            onClick = {},
            mostUsedLanguage = homePageViewState.last7DaysStats.mostUsedLanguage,
            mostUsedOs = homePageViewState.last7DaysStats.mostUsedOs,
            mostUsedEditor = homePageViewState.last7DaysStats.mostUsedEditor,
            currentStreak = homePageViewState.streaks.currentStreak,
        )
        Spacer(modifier = Modifier.height(spacing.medium))
    }
}

@Composable
private fun HomePageError(errorMessage: HomePageViewState.Error) = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomErrorAnimation,
    text = errorMessage.error.message,
    animationTestTag = HomePageTestTags.ERROR_ANIMATION_ILLUSTRATION
)

@Composable
private fun HomePageLoading() = WtaAnimation(
    animation = MaterialTheme.assets.animations.randomLoadingAnimation,
    text = "Loading..",
    animationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION
)

@Preview(
    apiLevel = 31,
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    apiLevel = 31,
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomePagePreview(
    @PreviewParameter(HomePagePreviewProvider::class) viewState: HomePageViewState,
) = WakaTimeAppTheme {
    HomePageContent(viewState = viewState, toDetailsPage = { })
}

class HomePagePreviewProvider : CollectionPreviewParameterProvider<HomePageViewState>(
    listOf(
        HomePageViewState.Loading,
        HomePageViewState.Error(Error.UnknownError("Something went wrong")),
        HomePageViewState.Loaded(
            last7DaysStats = Last7DaysStats(
                timeSpentToday = Time.ZERO,
                projectsWorkedOn = listOf(),
                weeklyTimeSpent = mapOf(),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = "",
            ),
            userDetails = HomePageUserDetails(
                photoUrl = "",
                fullName = "",
            ),
            streaks = Streaks(
                currentStreak = StreakRange.ZERO,
                longestStreak = StreakRange.ZERO,
            )
        ),
    )
)
