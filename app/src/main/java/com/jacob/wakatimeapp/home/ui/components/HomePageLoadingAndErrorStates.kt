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
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.ui.HomePageViewState.Error
import kotlin.random.Random

@Composable
fun HomePageLoading() =
    if (Random.nextBoolean()) ShowAnimation(
        animations = listOf(
            R.raw.loading_1,
            R.raw.loading_2,
            R.raw.loading_animation,
            R.raw.loading_bloob,
            R.raw.loading_paperplane_1,
            R.raw.loading_paperplane_2,
        ),
        text = "Loading.."
    ) else ShowIllustration(
        illustrations = listOf(
            R.drawable.il_loading_1,
            R.drawable.il_loading_2,
            R.drawable.il_loading_3,
            R.drawable.il_loading_3a,
        ),
        text = "Loading.."
    )

@Composable
fun HomePageError(errorMessage: Error) {
    ShowAnimation(
        animations = listOf(
            R.raw.error_1,
            R.raw.error_2,
            R.raw.error_animation,
        ),
        text = errorMessage.errorMessage
    )
}

@Composable
private fun ShowIllustration(illustrations: List<Int>, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = illustrations.random()),
            contentDescription = "",
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ShowAnimation(animations: List<Int>, text: String) {
    val composition by rememberLottieComposition(RawRes(animations.random()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun IllustrationPreview() = WakaTimeAppTheme {
    Surface {
        ShowIllustration(
            illustrations = listOf(
                R.drawable.il_loading_1,
                R.drawable.il_loading_2,
                R.drawable.il_loading_3,
                R.drawable.il_loading_3a,
            ),
            text = "Loading..."
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AnimationPreview() = WakaTimeAppTheme {
    ShowAnimation(
        animations = listOf(
            R.raw.loading_1,
            R.raw.loading_2,
            R.raw.loading_animation,
            R.raw.loading_bloob,
            R.raw.loading_paperplane_1,
            R.raw.loading_paperplane_2,
        ),
        text = "Loading..."
    )
}
