package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
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
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TabBar(
    pagerState: PagerState,
    pages: ImmutableList<Tabs>,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colorScheme.onSurface,
        divider = { },
        edgePadding = 0.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        pages.forEachIndexed { index, tab ->
            val isSelected = pagerState.currentPage == index
            Tab(
                selected = isSelected,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
    data object Time : Tabs("Time")
    data object Languages : Tabs("Languages")
    data object Editors : Tabs("Editors")
    data object OperatingSystems : Tabs("Operating Systems")
}
