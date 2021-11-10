package com.jacob.wakatimeapp.home.ui.components

import androidx.navigation.NavController
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.ui.HomePageViewModel
import com.jacob.wakatimeapp.home.ui.HomePageViewState
import com.jacob.wakatimeapp.home.ui.HomePageViewState.Loaded
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class HomePageParameters @ExperimentalCoroutinesApi constructor(
    val viewModel: HomePageViewModel,
    val navController: NavController,
)

data class HomePageErrorParameters(
    val errorMessage: HomePageViewState.Error,
)

data class HomePageLoadedParameters(
    val homePageViewState: Loaded,
    val navController: NavController,
)

data class ShowIllustrationParameters(
    val illustrations: List<Int>,
    val text: String,
    val illustrationTestTag: String,
    val textTestTag: String,
)

data class ShowAnimationParameters(
    val animations: List<Int>,
    val text: String,
    val animationTestTag: String,
    val textTestTag: String,
)

data class UserDetailsSectionParameters(
    val userDetails: UserDetails?,
)

data class OtherDailyStatsSectionParameters(
    val dailyStats: DailyStats?,
    val onClick: () -> Unit,
)

data class RecentProjectsParameters(
    val dailyStats: DailyStats?,
)

data class WeeklyReportParameters(
    val dailyStats: List<DailyStats>?,
)
