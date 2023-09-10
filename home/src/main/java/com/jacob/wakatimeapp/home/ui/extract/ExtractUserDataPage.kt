package com.jacob.wakatimeapp.home.ui.extract

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    var buttonHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.Center,
    ) {
        TransferringAnimation()
        when (viewState) {
            is ExtractPageViewState.Idle -> {
                CreateAndDownloadExtract(
                    createExtract = viewModel::createExtract,
                    updateButtonHeight = {
                        buttonHeight = with(localDensity) { it.size.height.toDp() }
                    },
                )
                LoadExtractFromFile()
            }

            is ExtractPageViewState.CreatingExtract -> {
                // show progress bar for creating extract
                AnimatedProgressButton(buttonHeight, 0f)
            }

            else -> Unit
        }
    }
}

@Composable
private fun TransferringAnimation() {
    // animation showing transferring or copying data
}

@Composable
private fun LoadExtractFromFile() {
    // open file picker to select extract
}
