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
import kotlin.random.Random

@Composable
fun HomePageLoading() =
    if (Random.nextBoolean()) ShowLoadingAnimation() else ShowLoadingIllustration()

@Composable
private fun ShowLoadingIllustration() {
    val illustrations = listOf(
        R.drawable.il_loading_1,
        R.drawable.il_loading_2,
        R.drawable.il_loading_3,
        R.drawable.il_loading_3a,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Image(
            painter = painterResource(id = illustrations.random()),
            contentDescription = "",
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Loading...",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ShowLoadingAnimation() {
    val illustrations = listOf(
        R.raw.loading_1,
        R.raw.loading_2,
        R.raw.loading_animation,
        R.raw.loading_bloob,
        R.raw.loading_paperplane_1,
        R.raw.loading_paperplane_2,
    )
    val composition by rememberLottieComposition(RawRes(illustrations.random()))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun IllustrationPreview() = WakaTimeAppTheme {
    Surface {
        ShowLoadingIllustration()
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AnimationPreview() = WakaTimeAppTheme {
    ShowLoadingAnimation()
}
