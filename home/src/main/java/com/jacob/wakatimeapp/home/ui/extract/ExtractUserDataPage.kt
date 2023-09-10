package com.jacob.wakatimeapp.home.ui.extract

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.extract.components.AnimatedProgressButton
import com.jacob.wakatimeapp.home.ui.extract.components.CreateAndDownloadExtract
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
    val localDensity = LocalDensity.current

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
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                    slideOutVertically { height -> -height } + fadeOut()
            },
        ) {
            when (it) {
                is ExtractPageViewState.Idle -> {
                    CreateAndDownloadExtract(
                        createExtract = viewModel::createExtract,
                    )
                    LoadExtractFromFile()
                }

                is ExtractPageViewState.CreatingExtract -> { // show progress bar for creating extract
                    AnimatedProgressButton((viewState as ExtractPageViewState.CreatingExtract).progress)
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
