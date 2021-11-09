package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.ui.HomePageTestTags.ERROR_ANIMATION_ILLUSTRATION
import com.jacob.wakatimeapp.home.ui.HomePageTestTags.ERROR_TEXT
import com.jacob.wakatimeapp.home.ui.HomePageTestTags.LOADING_ANIMATION_ILLUSTRATION
import com.jacob.wakatimeapp.home.ui.HomePageTestTags.LOADING_TEXT
import com.jacob.wakatimeapp.home.ui.HomePageViewState.Error
import kotlin.random.Random

@Composable
fun HomePageLoading() =
    if (Random.nextBoolean()) ShowAnimation(
        showAnimationParameters = ShowAnimationParameters(
            animations = listOf(
                R.raw.loading_1,
                R.raw.loading_2,
                R.raw.loading_animation,
                R.raw.loading_bloob,
                R.raw.loading_paperplane_1,
                R.raw.loading_paperplane_2,
            ),
            text = "Loading..",
            animationTestTag = LOADING_ANIMATION_ILLUSTRATION,
            textTestTag = LOADING_TEXT
        ),
    ) else ShowIllustration(
        showIllustrationParameters = ShowIllustrationParameters(
            illustrations = listOf(
                R.drawable.il_loading_1,
                R.drawable.il_loading_2,
                R.drawable.il_loading_3,
                R.drawable.il_loading_3a,
            ),
            text = "Loading..",
            illustrationTestTag = LOADING_ANIMATION_ILLUSTRATION,
            textTestTag = LOADING_TEXT
        ),
    )

@Composable
fun HomePageError(errorMessage: Error) {
    ShowAnimation(
        showAnimationParameters = ShowAnimationParameters(
            animations = listOf(
                R.raw.error_1,
                R.raw.error_2,
                R.raw.error_animation,
            ),
            text = errorMessage.errorMessage,
            animationTestTag = ERROR_ANIMATION_ILLUSTRATION,
            textTestTag = ERROR_TEXT
        ),
    )
}

@Composable
private fun ShowIllustration(
    showIllustrationParameters: ShowIllustrationParameters,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = showIllustrationParameters.illustrations.random()),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.testTag(showIllustrationParameters.illustrationTestTag)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = showIllustrationParameters.text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.testTag(showIllustrationParameters.textTestTag)
        )
    }
}

@Composable
fun ShowAnimation(
    showAnimationParameters: ShowAnimationParameters,
) {
    val composition by rememberLottieComposition(RawRes(showAnimationParameters.animations.random()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.testTag(showAnimationParameters.animationTestTag)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = showAnimationParameters.text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.testTag(showAnimationParameters.textTestTag)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun IllustrationPreview() = WakaTimeAppTheme {
    Surface {
        ShowIllustration(
            showIllustrationParameters = ShowIllustrationParameters(
                illustrations = listOf(
                    R.drawable.il_loading_1,
                    R.drawable.il_loading_2,
                    R.drawable.il_loading_3,
                    R.drawable.il_loading_3a,
                ),
                text = "Loading...",
                illustrationTestTag = "",
                textTestTag = ""
            ),
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AnimationPreview() = WakaTimeAppTheme {
    ShowAnimation(
        showAnimationParameters = ShowAnimationParameters(
            animations = listOf(
                R.raw.loading_1,
                R.raw.loading_2,
                R.raw.loading_animation,
                R.raw.loading_bloob,
                R.raw.loading_paperplane_1,
                R.raw.loading_paperplane_2,
            ),
            text = "Loading...",
            animationTestTag = "",
            textTestTag = ""
        )
    )
}
