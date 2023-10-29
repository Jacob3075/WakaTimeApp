package com.jacob.wakatimeapp.login.ui.loading

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets.Animations
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
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
internal fun LoadingPageContent(
    loginPageNavigator: LoginPageNavigator,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: LoadingPageViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        when (viewState) {
            is ViewState.LoggedOut -> loginPageNavigator.toLoginPage()
            is ViewState.LocalDbNotPopulated -> loginPageNavigator.toExtractUserDataPage()
            is ViewState.LocalDbPopulated -> loginPageNavigator.toHomePageFromLoading()
            is ViewState.Error -> TODO()
            else -> Unit
        }
    }
    WtaAnimation(animation = Animations.randomLoadingAnimation, text = "Loading...")
}
