package com.jacob.wakatimeapp.login.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import net.openid.appauth.AuthorizationException
import timber.log.Timber

@Composable
fun LoginPageContent(
    loginPageNavigator: LoginPageNavigator,
    modifier: Modifier = Modifier,
    viewModel: LoginPageViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel.authStatus) {
        if (viewModel.authStatus) {
            viewModel.updateUserDetails()
            loginPageNavigator.toHomePage()
        }
    }

    val launcher = authActivityResultLauncher(viewModel)
    val spacing = MaterialTheme.spacing

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = spacing.small, bottom = spacing.large)
    ) {
        AppTitle()
        LoginButton(onClick = { launcher.launch(viewModel.getAuthIntent()) })
    }
}

@Composable
private fun AppTitle() = Text(
    text = "Wakatime Client",
    style = TextStyle(
        fontSize = MaterialTheme.typography.h3.fontSize, // TODO
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Cursive
    )
)

@Composable
private fun authActivityResultLauncher(viewModel: LoginPageViewModel) =
    rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        result.data?.let(viewModel::exchangeToken) ?: run {
            Timber.e("Data not present")
            val ex = AuthorizationException.fromIntent(result.data!!)
            Timber.e(ex?.toJsonString())
        }
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
            .padding(horizontal = spacing.large + spacing.medium)
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
                text = "Login to Wakatime",
                style = MaterialTheme.typography.body1.copy(
                    // TODO
                    fontWeight = FontWeight.SemiBold,
                ),
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
