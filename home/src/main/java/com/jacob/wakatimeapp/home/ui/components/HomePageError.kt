package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.core.ui.R.raw
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.home.ui.HomePageTestTags
import com.jacob.wakatimeapp.home.ui.HomePageViewState.Error

@Composable
internal fun HomePageError(errorMessage: Error) = WtaAnimation(
    animations = listOf(
        raw.error_1,
        raw.error_2,
        raw.error_animation,
    ),
    text = errorMessage.errorMessage,
    animationTestTag = HomePageTestTags.ERROR_ANIMATION_ILLUSTRATION,
)
