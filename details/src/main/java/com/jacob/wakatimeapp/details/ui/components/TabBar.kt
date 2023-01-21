package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBar(pagerState: PagerState, pages: List<Tabs>) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colorScheme.onSurface,
        divider = { },
        edgePadding = 0.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        pages.forEachIndexed { index, tab ->
            val isSelected = pagerState.currentPage == index
            Tab(
                selected = isSelected,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
            ) {
                Text(
                    text = tab.title,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                )
            }
        }
    }
}

sealed class Tabs(val title: String) {
    object Time : Tabs("Time")
    object Languages : Tabs("Languages")
    object Editors : Tabs("Editors")
    object OperatingSystems : Tabs("Operating Systems")
}
