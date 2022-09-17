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
import com.jacob.wakatimeapp.core.ui.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.home.ui.components.*
import kotlinx.coroutines.launch

interface HomePageNavigator {
    fun toDetailsPage()
}

@Composable
fun HomePageContent(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel = hiltViewModel(),
    navigator: HomePageNavigator,
    scaffoldState: ScaffoldState,
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
                navigator = navigator,
            )

            is HomePageViewState.Error -> HomePageError(viewState as HomePageViewState.Error)
        }
    }
}

@Composable
private fun HomePageLoaded(
    homePageViewState: HomePageViewState.Loaded,
    navigator: HomePageNavigator,
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
            onClick = navigator::toDetailsPage,
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
