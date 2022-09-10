package com.jacob.wakatimeapp.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.common.observeInLifecycle
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.TimeSpentCardParameters
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.home.ui.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Destination
@Composable
private fun HomePageContent(
    navigator: DestinationsNavigator,
    parameters: HomePageParameters,
) {
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewModel = parameters.viewModel
    val viewState by viewModel.homePageState.collectAsState()

    viewModel.errors
        .onEach {
            snackBarCoroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = it.errorMessage,
                    actionLabel = "Action",
                    duration = Long
                )
            }
        }
        .observeInLifecycle(LocalLifecycleOwner.current)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        when (viewState) {
            is HomePageViewState.Loading -> HomePageLoading()
            is HomePageViewState.Loaded -> HomePageLoaded(
                HomePageLoadedParameters(
                    homePageViewState = viewState as HomePageViewState.Loaded,
                    navController = parameters.navController
                ),
                homePageNavigations = viewModel.homePageNavigations,
            )

            is HomePageViewState.Error -> HomePageError(HomePageErrorParameters(viewState as HomePageViewState.Error))
        }
    }
}

@Composable
private fun HomePageLoaded(
    parameters: HomePageLoadedParameters,
    homePageNavigations: HomePageNavigations,
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
                onClick = {
                    parameters.navController.navigate(homePageNavigations.toDetailsPage())
                }
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
