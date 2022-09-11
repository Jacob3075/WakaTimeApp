package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.TimeSpentCardParameters
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.home.ui.components.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomePageContent(
    viewModel: HomePageViewModel = hiltViewModel(),
    homePageNavigator: HomePageNavigator,
) {
    Timber.e("Loaded")
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by viewModel.homePageState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.errors
            .collectLatest {
                snackBarCoroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.errorMessage,
                        actionLabel = "Action",
                        duration = Long
                    )
                }
            }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        when (viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                HomePageLoadedParameters(
                    homePageViewState = viewState as HomePageViewState.Loaded,
                    navigator = homePageNavigator
                ),
                homePageNavigator = homePageNavigator,
            )

            is HomePageViewState.Error -> HomePageError(HomePageErrorParameters(viewState as HomePageViewState.Error))
        }
    }
}

@Composable
private fun HomePageLoaded(
    parameters: HomePageLoadedParameters,
    homePageNavigator: HomePageNavigator,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(UserDetailsSectionParameters(parameters.homePageViewState.userDetails))
        Spacer(modifier = Modifier.height(25.dp))

        TimeSpentCard(
            parameters = TimeSpentCardParameters(
                gradient = Gradients.primary,
                roundedCornerPercent = 25,
                iconId = drawable.ic_time,
                mainText = "Total Time Spent Today",
                time = parameters.homePageViewState.contentData.todaysStats.timeSpent,
                onClick = homePageNavigator::toDetailsPage
            )
        )
        Spacer(modifier = Modifier.height(25.dp))

        RecentProjects(RecentProjectsParameters(parameters.homePageViewState.contentData.todaysStats))
        Spacer(modifier = Modifier.height(10.dp))

        WeeklyReport(WeeklyReportParameters(parameters.homePageViewState.contentData.dailyStats))
        Spacer(modifier = Modifier.height(25.dp))

        OtherDailyStatsSection(OtherDailyStatsSectionParameters(
            parameters.homePageViewState.contentData.todaysStats,
            onClick = {}
        ))
        Spacer(modifier = Modifier.height(25.dp))
    }
}
