package com.jacob.wakatimeapp.details.ui.modiffiers

import androidx.compose.material3.TabPosition
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

/**
 * [See Issue](https://github.com/google/accompanist/issues/1076)
 * [Source](https://github.com/c5inco/Material3Pager/blob/main/app/src/main/java/com/c5inco/material3pager/PagerTab.kt)
 */
@ExperimentalPagerApi
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) return@layout layout(constraints.maxWidth, 0) {}

    val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
    val currentTab = tabPositions[currentPage]
    val previousTab = tabPositions.getOrNull(currentPage - 1)
    val nextTab = tabPositions.getOrNull(currentPage + 1)
    val fraction = pagerState.currentPageOffset

    val indicatorWidth = when {
        fraction > 0 && nextTab != null -> lerp(
            currentTab.width,
            nextTab.width,
            fraction,
        ).roundToPx()

        fraction < 0 && previousTab != null -> lerp(
            currentTab.width,
            previousTab.width,
            -fraction,
        ).roundToPx()

        else -> currentTab.width.roundToPx()
    }

    val indicatorOffset = when {
        fraction > 0 && nextTab != null -> lerp(
            currentTab.left,
            nextTab.left,
            fraction,
        ).roundToPx()

        fraction < 0 && previousTab != null -> lerp(
            currentTab.left,
            previousTab.left,
            -fraction,
        ).roundToPx()

        else -> currentTab.left.roundToPx()
    }

    val placeable = measurable.measure(
        Constraints(
            minWidth = indicatorWidth,
            maxWidth = indicatorWidth,
            minHeight = 0,
            maxHeight = constraints.maxHeight,
        ),
    )

    return@layout layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
        placeable.placeRelative(
            indicatorOffset,
            maxOf(constraints.minHeight - placeable.height, 0),
        )
    }
}
