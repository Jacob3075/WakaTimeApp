package com.jacob.wakatimeapp.home.ui.extract

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
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

    Column {
        TransferringAnimation()
        when (viewState) {
            is ExtractPageViewState.Idle -> {
                CreateAndDownloadExtract(null, viewModel::createExtract)
                LoadExtractFromFile()
            }

            is ExtractPageViewState.CreatingExtract -> {
                // show progress bar for creating extract
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
