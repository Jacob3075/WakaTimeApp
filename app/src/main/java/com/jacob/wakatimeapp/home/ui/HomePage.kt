package com.jacob.wakatimeapp.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jacob.wakatimeapp.R.drawable
import com.jacob.wakatimeapp.core.ui.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.TimeSpentCardParameters
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.common.observeInLifecycle
import com.jacob.wakatimeapp.home.ui.components.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomePage : Fragment() {
    private val viewModel by viewModels<HomePageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            WakaTimeAppTheme {
                HomePageContent(HomePageParameters(
                    viewModel = viewModel,
                    navController = findNavController()
                ))
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun HomePageContent(parameters: HomePageParameters) {
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by parameters.viewModel.homePageState.collectAsState()

    parameters.viewModel.errors
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
            is HomePageViewState.Loaded -> HomePageLoaded(HomePageLoadedParameters(
                homePageViewState = viewState as HomePageViewState.Loaded,
                navController = parameters.navController
            ))
            is HomePageViewState.Error -> HomePageError(HomePageErrorParameters(viewState as HomePageViewState.Error))
        }
    }
}

@Composable
private fun HomePageLoaded(parameters: HomePageLoadedParameters) {
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
                    parameters.navController.navigate(HomePageDirections.homePageToDetailsPage())
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
