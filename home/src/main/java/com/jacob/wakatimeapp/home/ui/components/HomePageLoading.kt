package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.components.WtaIllustration
import com.jacob.wakatimeapp.home.R.drawable
import com.jacob.wakatimeapp.home.R.raw
import com.jacob.wakatimeapp.home.ui.HomePageTestTags
import kotlin.random.Random

@Composable
internal fun HomePageLoading() =
    if (Random.nextBoolean()) WtaAnimation(
        animations = listOf(
            raw.loading_1,
            raw.loading_2,
            raw.loading_animation,
            raw.loading_bloob,
            raw.loading_paperplane_1,
            raw.loading_paperplane_2,
        ),
        text = "Loading..",
        animationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION,
    ) else WtaIllustration(
        illustrations = listOf(
            drawable.il_loading_1,
            drawable.il_loading_2,
            drawable.il_loading_3,
            drawable.il_loading_3a,
        ),
        text = "Loading..",
        illustrationTestTag = HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION,
    )
