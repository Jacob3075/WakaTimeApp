package com.jacob.wakatimeapp.login.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import net.openid.appauth.AuthorizationException
import timber.log.Timber

interface LoginPageNavigator {
    fun toHomePage()
}

@Composable
fun LoginPageContent(
    viewModel: LoginPageViewModel = hiltViewModel(),
    loginPageNavigator: LoginPageNavigator,
) {
    LaunchedEffect(viewModel.authStatus) {
        if (viewModel.authStatus) {
            viewModel.updateUserDetails()
            loginPageNavigator.toHomePage()
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let(viewModel::exchangeToken) ?: run {
                Timber.e("Data not present")
                val ex = AuthorizationException.fromIntent(result.data!!)
                Timber.e(ex?.toJsonString())
            }
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text(
            text = "Wakatime Client",
            modifier = Modifier.padding(top = 2.dp),
            style = TextStyle(
                fontSize = MaterialTheme.typography.h3.fontSize,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
            )
        )
        LoginButton {
            launcher.launch(viewModel.getAuthIntent())
        }
    }
}

@Composable
private fun LoginButton(
    onClick: () -> Unit,
) {
    val loginButtonGradient =
        Brush.horizontalGradient(listOf(Gradients.primary.startColor, Gradients.primary.endColor))
    val buttonShape = RoundedCornerShape(45)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = buttonShape,
        contentPadding = PaddingValues(),
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(bottom = 50.dp)
            .shadow(12.dp, shape = buttonShape)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(loginButtonGradient, buttonShape)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Login to Wakatime",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(14.dp)
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
