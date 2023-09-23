package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.home.ui.extract.ExtractPageViewState as ViewState
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets.Animations
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
        AnimatedContent(
            targetState = viewState,
            label = "",
            transitionSpec = { createContentTransform() },
            modifier = Modifier,
            contentKey = { it is ViewState.Error },
        ) {
            when (it) {
                is ViewState.Error -> ExtractStateAnimation(
                    Animations.randomErrorAnimation,
                    it.error.toString(),
                )

                else -> ExtractStateAnimation(Animations.randomDataTransferAnimations, "")
            }
        }

        AnimatedContent(
            targetState = viewState,
            label = "",
            transitionSpec = { createContentTransform() },
            modifier = Modifier,
            contentKey = { it.javaClass },
        ) {
            when (it) {
                is ViewState.Idle, is ViewState.Error -> IdleContent(
                    createExtract = viewModel::createExtract,
                    downloadExistingExtract = viewModel::downloadExistingExtract,
                    loadFromFile = viewModel::demo,
                )

                is ViewState.CreatingExtract -> AnimatedProgressBar(it.progress)
                is ViewState.ExtractCreated -> Button(
                    onClick = { viewModel.downloadExtract(it.createdExtract) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Download Extract")
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun IdleContent(
    createExtract: () -> Unit,
    loadFromFile: () -> Unit,
    downloadExistingExtract: () -> Unit,
) = Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Button(
        onClick = createExtract,
        modifier = Modifier.fillMaxWidth(),
    ) { Text(text = "Create Extract") }

    OutlinedButton(
        onClick = downloadExistingExtract,
        modifier = Modifier.fillMaxWidth(),
    ) { Text(text = "Download Existing Extract") }

    OutlinedButton(
        onClick = loadFromFile,
        modifier = Modifier.fillMaxWidth(),
    ) { Text(text = "Load Extract From File") }
}

@Composable
private fun ExtractStateAnimation(@RawRes animationResourceId: Int, text: String) = WtaAnimation(
    animation = animationResourceId,
    text = text,
    speed = 0.7f,
    modifier = Modifier
        .fillMaxWidth(1f)
        .fillMaxHeight(fraction = 0.5f),
)

private fun AnimatedContentTransitionScope<ViewState>.createContentTransform() = slideIntoContainer(
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
