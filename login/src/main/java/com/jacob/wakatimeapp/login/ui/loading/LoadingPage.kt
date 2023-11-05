package com.jacob.wakatimeapp.login.ui.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets.Animations
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@Destination(start = true)
fun LoadingPage(
    loginPageNavigator: LoginPageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) = LoadingPageContent(
    loginPageNavigator = loginPageNavigator,
    snackbarHostState = snackbarHostState,
    modifier = modifier,
)

@Composable
private fun LoadingPageContent(
    loginPageNavigator: LoginPageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: LoadingPageViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewState) {
        when (val viewStateInstance = viewState) {
            is LoadingPageViewState.LoggedOut -> loginPageNavigator.toLoginPage()
            is LoadingPageViewState.LocalDbNotPopulated -> loginPageNavigator.toExtractUserDataPage()
            is LoadingPageViewState.LocalDbPopulated -> loginPageNavigator.toHomePageFromLoading()
            is LoadingPageViewState.Error -> {
                showSnackBar(
                    viewStateInstance,
                    snackBarCoroutineScope,
                    snackbarHostState,
                )
                viewModel.logout()
            }

            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val text = when (val viewStateInstance = viewState) {
            is LoadingPageViewState.Error -> viewStateInstance.error.errorDisplayMessage()
            else -> "Loading..."
        }
        WtaAnimation(animation = Animations.randomLoadingAnimation, text = text, modifier = modifier)
    }
}

private fun showSnackBar(
    viewState: LoadingPageViewState.Error,
    snackBarCoroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) = snackBarCoroutineScope.launch {
    snackbarHostState.showSnackbar(
        message = viewState.error.message,
        duration = SnackbarDuration.Long,
    )
}
