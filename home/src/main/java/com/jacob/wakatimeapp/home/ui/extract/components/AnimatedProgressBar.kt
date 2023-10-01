package com.jacob.wakatimeapp.home.ui.extract.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
@Suppress("MagicNumber")
internal fun AnimatedProgressBar(progressValue: Float) {
    val roundedCornerShape = RoundedCornerShape(percent = 50)
    val spacing = MaterialTheme.spacing
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.large)
                .clip(roundedCornerShape)
                .background(color = Color.Transparent)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = roundedCornerShape,
                ),
        ) {
            Box(
                modifier = Modifier
                    .animateExpandWidth(progressValue)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {
            }
        }
        Spacer(modifier = Modifier.height(spacing.sMedium))
        val formattedProgress = String.format(Locale.getDefault(), "%.2f", progressValue * 100)
        Text(text = "Creating Extract.... ($formattedProgress%)")
    }
}

@WtaComponentPreviews
@Composable
private fun AnimatedProgressBarPreview() {
    var progressValue by remember { mutableFloatStateOf(0.5f) }

    LaunchedEffect(Unit) {
        while (progressValue < 1f) {
            delay(300)
            progressValue += 0.04f
        }

        if (progressValue >= 1f) {
            progressValue = 1f
        }
    }

    WakaTimeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 50.dp),
        ) {
            AnimatedProgressBar(progressValue)
        }
    }
}

private fun Modifier.animateExpandWidth(progressValue: Float): Modifier = composed {
    val progress = remember {
        Animatable(0f)
    }

    LaunchedEffect(progressValue) {
        progress.animateTo(
            targetValue = progressValue,
            animationSpec = ProgressAnimationSpec,
        )
    }

    this.fillMaxWidth(progress.value)
}
