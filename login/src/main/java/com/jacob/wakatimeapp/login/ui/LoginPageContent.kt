package com.jacob.wakatimeapp.login.ui

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.modifiers.gesturesDisabled
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.pageTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginPageContent(
    loginPageNavigator: LoginPageNavigator,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    viewModel: LoginPageViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val snackBarCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewState) {
        when (val viewStateInstance = viewState) {
            is LoginPageState.Success -> {
                viewModel.updateUserDetails()
                loginPageNavigator.toHomePage()
            }

            is LoginPageState.Error -> showSnackBar(
                viewStateInstance,
                snackBarCoroutineScope,
                scaffoldState
            )

            else -> Unit
        }
    }

    val launcher = authActivityResultLauncher(viewModel)

    when (viewState) {
        is LoginPageState.Idle, is LoginPageState.Error -> LoginPageIdleState(
            viewModel,
            launcher = launcher
        )

        is LoginPageState.Loading -> Box(
            modifier = Modifier.fillMaxSize()
                .gesturesDisabled()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
                    .size(60.dp),
            )
            LoginPageIdleState(viewModel, launcher = launcher)
        }

        else -> Unit
    }
}

private fun showSnackBar(
    viewState: LoginPageState.Error,
    snackBarCoroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
) = snackBarCoroutineScope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
        message = viewState.message,
        duration = SnackbarDuration.Long
    )
}

@Composable
private fun LoginPageIdleState(
    viewModel: LoginPageViewModel,
    modifier: Modifier = Modifier,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {
    val spacing = MaterialTheme.spacing

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = spacing.small, bottom = spacing.large)
            .padding(horizontal = spacing.small)
    ) {
        AppTitle()
        LoginButton(onClick = { launcher.launch(viewModel.getAuthIntent()) })
    }
}

@Composable
private fun AppTitle() = Text(
    text = "Wakatime Client",
    style = MaterialTheme.typography.pageTitle,
)

@Composable
private fun authActivityResultLauncher(viewModel: LoginPageViewModel) =
    rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val data = result.data
        if (data == null) {
            viewModel.authDataNotFound(result.data!!)
            return@rememberLauncherForActivityResult
        }
        data.let(viewModel::exchangeToken)
    }

@Composable
private fun LoginButton(
    onClick: () -> Unit,
) {
    val loginButtonGradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.gradients.primary.startColor,
            MaterialTheme.gradients.primary.endColor,
        )
    )
    val buttonShape = RoundedCornerShape(percent = 45)
    val spacing = MaterialTheme.spacing
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = buttonShape,
        contentPadding = PaddingValues(),
        modifier = Modifier
            .padding(horizontal = spacing.large)
            .shadow(elevation = 8.dp, shape = buttonShape, clip = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(loginButtonGradient, buttonShape)
                .padding(vertical = spacing.small)
        ) {
            Text(
                text = "Login to Wakatime".uppercase(),
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(spacing.medium)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoginButtonPreview() = WakaTimeAppTheme { LoginButton {} }

@Preview(showBackground = true)
@Composable
private fun LoginButtonPreview2() = WakaTimeAppTheme { LoginButton {} }
