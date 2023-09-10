package com.jacob.wakatimeapp.home.ui.extract.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import kotlinx.coroutines.delay

@Composable
internal fun CreateAndDownloadExtract(progressValue: Float?, createExtract: () -> Unit) {
    // Create element height in dp state
    val localDensity = LocalDensity.current
    var columnHeightDp by remember { mutableStateOf(0.dp) }

    if (progressValue == null) {
        Button(
            onClick = createExtract,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                },
        ) {
            Text(text = "Create Extract")
        }
    } else {
        AnimatedProgressButton(columnHeightDp, progressValue)
    }
}

@Composable
private fun AnimatedProgressButton(
    columnHeightDp: Dp,
    progressValue: Float,
) {
    val pxToMove = with(LocalDensity.current) { 15.dp.toPx() }
    val animatePosition = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        animatePosition.animateTo(
            targetValue = pxToMove,
            animationSpec = tween(durationMillis = 200),
        )
    }

    Box(
        modifier = Modifier
            .offset(x = 0.dp, y = animatePosition.value.dp)
            .fillMaxWidth()
            .height(columnHeightDp)
            .background(color = Color.Transparent)
            .clip(RoundedCornerShape(percent = 50)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressValue / 100f)
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary),
        ) {
        }
    }
}

@WtaComponentPreviews
@Composable
private fun CreateAndDownloadExtractPreview() {
    WakaTimeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CreateAndDownloadExtract(progressValue = null) {}
        }
    }
}

@WtaComponentPreviews
@Composable
private fun CreateAndDownloadExtractPreviewWithValue() {
    var progressValue: Float? by remember { mutableStateOf(null) }

    WakaTimeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 50.dp),
        ) {
            CreateAndDownloadExtract(progressValue) {
                progressValue = 0.5f
            }
        }
    }
}

@WtaComponentPreviews
@Composable
fun AnimatedProgressButtonPreview() {
    var progressValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            while (progressValue < 100) {
                progressValue++
                delay(50)
            }

            if (progressValue >= 100f) {
                progressValue = 0f
            }
        }
    }

    WakaTimeAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 50.dp),
        ) {
            AnimatedProgressButton(50.dp, progressValue)
        }
    }
}
