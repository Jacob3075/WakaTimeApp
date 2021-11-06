package com.jacob.wakatimeapp.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration.Long
import androidx.compose.material.Text
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
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.common.utils.observeInLifecycle
import com.jacob.wakatimeapp.home.ui.components.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

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
                HomePageContent(viewModel)
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun HomePageContent(viewModel: HomePageViewModel) {
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    val viewState by viewModel.homePageState.collectAsState()
    Timber.e(viewState.javaClass.name)

    viewModel.errors
        .onEach {
            snackBarCoroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Message",
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
            is HomePageViewState.Loaded -> HomePageLoaded(viewState as HomePageViewState.Loaded)
            is HomePageViewState.Error -> HomePageError()
        }
    }
}

@Composable
private fun HomePageLoaded(homePageViewState: HomePageViewState.Loaded) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .verticalScroll(scrollState)
    ) {
        UserDetailsSection(homePageViewState.userDetails)
        Spacer(modifier = Modifier.height(25.dp))
        TimeSpentSection(homePageViewState.contentData.todaysStats)
        Spacer(modifier = Modifier.height(25.dp))
        RecentProjects(homePageViewState.contentData.todaysStats)
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyReport(homePageViewState.contentData.dailyStats)
        Spacer(modifier = Modifier.height(25.dp))
        OtherDailyStats(homePageViewState.contentData.todaysStats)
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
private fun HomePageError() {
    Text(text = "ERROR")
}
