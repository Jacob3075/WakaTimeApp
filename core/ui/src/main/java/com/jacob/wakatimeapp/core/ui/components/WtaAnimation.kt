package com.jacob.wakatimeapp.core.ui.components

import androidx.annotation.RawRes as XmlRawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun WtaAnimation(
    @XmlRawRes animation: Int,
    text: String,
    animationTestTag: String,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(RawRes(animation))

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
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lMedium))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
