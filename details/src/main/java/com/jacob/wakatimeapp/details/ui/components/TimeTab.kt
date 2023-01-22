package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
internal fun TimeTab(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier.fillMaxSize(),
    ) {
        RecentTimeSpentChart()
        QuickStatsCards()
        ProjectHistory()
    }
}

@Composable
internal fun RecentTimeSpentChart() {
    Text(text = "Recent Time Spent Chart")
}

@Composable
internal fun QuickStatsCards() {
    Text(text = "Quick Stats Cards")
}

@Composable
internal fun ProjectHistory() {
    Text(text = "Project History")
}
