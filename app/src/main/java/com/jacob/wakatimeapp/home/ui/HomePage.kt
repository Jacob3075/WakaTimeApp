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
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.utils.observeInLifecycle
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
                HomePageContent(HomePageParameter(viewModel))
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun HomePageContent(parameter: HomePageParameter) {
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()
    val viewState by parameter.viewModel.homePageState.collectAsState()

    parameter.viewModel.errors
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
            is HomePageViewState.Loaded -> HomePageLoaded(HomePageLoadedParameters(viewState as HomePageViewState.Loaded))
            is HomePageViewState.Error -> HomePageError(HomePageErrorParameters(viewState as HomePageViewState.Error))
        }
    }
}

@Composable
private fun HomePageLoaded(parameter: HomePageLoadedParameters) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(UserDetailsSectionParameters(parameter.homePageViewState.userDetails))
        Spacer(modifier = Modifier.height(25.dp))
        TimeSpentSection(TimeSpentSectionParameters(parameter.homePageViewState.contentData.todaysStats))
        Spacer(modifier = Modifier.height(25.dp))
        RecentProjects(RecentProjectsParameters(parameter.homePageViewState.contentData.todaysStats))
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyReport(WeeklyReportParameters(parameter.homePageViewState.contentData.dailyStats))
        Spacer(modifier = Modifier.height(25.dp))
        OtherDailyStatsSection(OtherDailyStatsSectionParameters(parameter.homePageViewState.contentData.todaysStats))
        Spacer(modifier = Modifier.height(25.dp))
    }
}
