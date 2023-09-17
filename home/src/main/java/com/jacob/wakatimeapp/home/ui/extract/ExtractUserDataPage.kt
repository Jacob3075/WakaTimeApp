package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.home.ui.extract.ExtractPageViewState as ViewState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.extract.ExtractUseDataViewModel.Constants.AnimationDuration
import com.jacob.wakatimeapp.home.ui.extract.components.AnimatedProgressBar
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun ExtractUserDataPage(
    navigator: HomePageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) = ExtractUserDataScreen(navigator, snackbarHostState, modifier, hiltViewModel())

@Composable
private fun ExtractUserDataScreen(
    navigator: HomePageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ExtractUseDataViewModel = hiltViewModel(),
) {
    val viewState by viewModel.extractPageState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TransferringAnimation()
        AnimatedContent(
            targetState = viewState,
            label = "",
            transitionSpec = { createContentTransform() },
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.large),
            contentKey = { it.javaClass },
        ) {
            when (it) {
                is ViewState.Idle -> {
                    CreateExtractButton(viewModel::createExtract)
                    LoadExtractFromFile()
                }

                is ViewState.CreatingExtract -> AnimatedProgressBar(it.progress)
                is ViewState.ExtractCreated -> DownloadExtractButton(downloadExtract = viewModel::downloadExtract)
                else -> Unit
            }
        }
    }
}

private fun AnimatedContentTransitionScope<ViewState>.createContentTransform() =
    slideIntoContainer(
        towards = SlideDirection.Up,
        animationSpec = tween(
            durationMillis = AnimationDuration,
            easing = LinearEasing,
        ),
    ) togetherWith slideOutOfContainer(
        towards = SlideDirection.Up,
        animationSpec = tween(
            durationMillis = AnimationDuration,
            easing = LinearEasing,
        ),
    )

@Composable
private fun TransferringAnimation() { // animation showing transferring or copying data
}

@Composable
private fun LoadExtractFromFile() { // open file picker to select extract
}

@Composable
private fun CreateExtractButton(
    createExtract: () -> Unit,
    modifier: Modifier = Modifier,
) = Button(
    onClick = createExtract,
    modifier = modifier.fillMaxWidth(),
) {
    Text(text = "Create Extract")
}

@Composable
private fun DownloadExtractButton(
    downloadExtract: () -> Unit,
    modifier: Modifier = Modifier,
) = Button(
    onClick = downloadExtract,
    modifier = modifier.fillMaxWidth(),
) {
    Text(text = "Download Extract")
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
            CreateExtractButton(createExtract = {})
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
            CreateExtractButton(createExtract = { progressValue = 0.5f })
        }
    }
}
