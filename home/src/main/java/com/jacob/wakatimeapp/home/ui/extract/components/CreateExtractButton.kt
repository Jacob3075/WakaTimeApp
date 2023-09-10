package com.jacob.wakatimeapp.home.ui.extract.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.coroutines.delay

@Composable
internal fun CreateAndDownloadExtract(
    createExtract: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Create element height in dp state
    Button(
        onClick = createExtract,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = "Create Extract")
    }
}

@Composable
internal fun AnimatedProgressButton(
    progressValue: Float,
) {
    val spacing = MaterialTheme.spacing
    val roundedCornerShape = RoundedCornerShape(percent = 50)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.large)
                .background(color = Color.Transparent)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = roundedCornerShape,
                )
                .clip(roundedCornerShape),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressValue)
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {
            }
        }
        Spacer(modifier = Modifier.height(spacing.sMedium))
        Text(text = "Creating Extract....")
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
            CreateAndDownloadExtract(createExtract = {})
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
            CreateAndDownloadExtract(createExtract = { progressValue = 0.5f })
        }
    }
}

@WtaComponentPreviews
@Composable
fun AnimatedProgressButtonPreview() {
    var progressValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            while (progressValue < 1f) {
                progressValue += 0.02f
                delay(1000)
            }

            if (progressValue >= 1f) {
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
            AnimatedProgressButton(progressValue)
        }
    }
}
