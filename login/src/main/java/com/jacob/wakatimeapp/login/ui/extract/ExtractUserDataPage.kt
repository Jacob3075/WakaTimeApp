package com.jacob.wakatimeapp.login.ui.extract

import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState as ViewState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets.Animations
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.CreatingExtract
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.DownloadingExtract
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.Error
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.ExtractCreated
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.ExtractLoaded
import com.jacob.wakatimeapp.login.ui.extract.ExtractPageViewState.Idle
import com.jacob.wakatimeapp.login.ui.extract.ExtractUseDataViewModel.Constants.AnimationDuration
import com.jacob.wakatimeapp.login.ui.extract.components.AnimatedProgressBar
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@Composable
@Destination
fun ExtractUserDataPage(
    navigator: ExtractUserDataNavigator,
    modifier: Modifier = Modifier,
) = ExtractUserDataScreen(navigator, modifier, hiltViewModel())

@Composable
private fun ExtractUserDataScreen(
    navigator: ExtractUserDataNavigator,
    modifier: Modifier = Modifier,
    viewModel: ExtractUseDataViewModel = hiltViewModel(),
) {
    val viewState by viewModel.extractPageState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewState) {
        Timber.d("viewState: $viewState")
    }

    val launcher = rememberLauncherForActivityResult(GetContent()) {
        val item = context.contentResolver.openInputStream(it!!)
        val bytes = item?.readBytes()
        item?.close()

        viewModel.loadExtracted(bytes)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExtractCreationAnimation(viewState)

        AnimatedContent(
            targetState = viewState,
            label = "",
            transitionSpec = { createContentTransform() },
            modifier = Modifier,
            contentKey = { it.javaClass },
        ) {
            when (it) {
                is Idle, is Error -> IdleContent(
                    createExtract = viewModel::createExtract,
                    downloadExistingExtract = viewModel::downloadExistingExtract,
                    filePicker = { launcher.launch("application/json") },
                )

                is CreatingExtract -> AnimatedProgressBar(it.progress)
                is ExtractCreated -> Button(
                    onClick = { viewModel.downloadExtract(it.createdExtract) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Download Extract")
                }

                is DownloadingExtract -> Text(text = "Downloading Extract...")
                is ExtractLoaded -> navigator.toHomePageFromExtractUserData()
            }
        }
    }
}

@Composable
private fun ExtractCreationAnimation(viewState: ViewState) {
    AnimatedContent(
        targetState = viewState,
        label = "",
        transitionSpec = { createContentTransform() },
        modifier = Modifier,
        contentKey = { it is Error },
    ) {
        when (it) {
            is Error -> ExtractStateAnimation(
                Animations.randomErrorAnimation,
                it.error.errorDisplayMessage(),
            )

            else -> ExtractStateAnimation(Animations.randomDataTransferAnimations, "")
        }
    }
}

@Composable
private fun IdleContent(
    createExtract: () -> Unit,
    filePicker: () -> Unit,
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
        onClick = filePicker,
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
