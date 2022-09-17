package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.components.cards.TimeSpentCard
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.HomePageViewState.Loaded

@Composable
internal fun HomePageLoaded(
    homePageViewState: Loaded,
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
