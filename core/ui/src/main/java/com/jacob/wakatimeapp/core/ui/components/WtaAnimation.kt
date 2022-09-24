package com.jacob.wakatimeapp.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun WtaAnimation(
    animations: List<Int>,
    text: String,
    animationTestTag: String,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(RawRes(animations.random()))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.testTag(animationTestTag)
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    }
}
