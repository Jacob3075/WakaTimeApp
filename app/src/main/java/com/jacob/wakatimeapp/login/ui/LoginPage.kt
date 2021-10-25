package com.jacob.wakatimeapp.login.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.jacob.wakatimeapp.common.ui.theme.Gradients
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import timber.log.Timber

@AndroidEntryPoint
class LoginPage : Fragment() {
    private val viewModel by viewModels<LoginPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            if (viewModel.isLoggedIn()) findNavController(this@LoginPage)
                .navigate(LoginPageDirections.loginPageToHomePage())

            WakaTimeAppTheme {
                LoginPageContent(
                    viewModel,
                    findNavController(this@LoginPage)
                )
            }
        }
    }
}

@Composable
private fun LoginPageContent(
    viewModel: LoginPageViewModel,
    navController: NavController,
) = Surface(
    modifier = Modifier.fillMaxSize(),
) {
    val authService = AuthorizationService(LocalContext.current)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                viewModel.exchangeToken(authService, it)
                viewModel.updateUserDetails(authService)
                navController.navigate(LoginPageDirections.loginPageToHomePage())
            } ?: run {
                Timber.e("Data not present")
                val ex = AuthorizationException.fromIntent(result.data!!)
                Timber.e(ex?.toJsonString())
            }
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Wakatime Client",
            modifier = Modifier.padding(top = 72.dp),
            style = TextStyle(
                fontSize = MaterialTheme.typography.h3.fontSize,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
            )
        )
        LoginButton {
            val authIntent = authService.getAuthorizationRequestIntent(viewModel.authRequest)
            launcher.launch(authIntent)
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
