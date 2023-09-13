package com.jacob.wakatimeapp.home.ui.extract

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.extract.components.AnimatedProgressBar
import com.ramcosta.composedestinations.annotation.Destination

const val AnimationDuration = 250

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
    ) {
        TransferringAnimation()
        AnimatedContent(
            targetState = viewState,
            label = "",
            transitionSpec = {
                (
                    slideIntoContainer(
                        towards = SlideDirection.Up,
                        animationSpec = tween(
                            durationMillis = AnimationDuration,
                            easing = EaseInOut,
                        ),
                    ) + fadeIn(tween(durationMillis = AnimationDuration, easing = EaseInOut))
                    ) togetherWith
                    slideOutOfContainer(
                        towards = SlideDirection.Up,
                        animationSpec = tween(
                            durationMillis = AnimationDuration,
                            easing = EaseInOut,
                        ),
                    ) + fadeOut(tween(durationMillis = AnimationDuration, easing = EaseInOut))
            },
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.large),
        ) {
            when (it) {
                is ExtractPageViewState.Idle -> {
                    CreateAndDownloadExtract(viewModel::createExtract)
                    LoadExtractFromFile()
                }

                is ExtractPageViewState.CreatingExtract -> { // show progress bar for creating extract
                    AnimatedProgressBar((viewState as ExtractPageViewState.CreatingExtract).progress)
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun TransferringAnimation() { // animation showing transferring or copying data
}

@Composable
private fun LoadExtractFromFile() { // open file picker to select extract
}

@Composable
private fun CreateAndDownloadExtract(
    createExtract: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = createExtract,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = "Create Extract")
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
