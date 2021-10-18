package com.jacob.wakatimeapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jacob.wakatimeapp.theme.WakaTimeAppTheme
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
    ): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                WakaTimeAppTheme {
                    LoginPageContent(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginPageContent(viewModel: LoginPageViewModel) = Surface(
    modifier = Modifier.fillMaxSize(),
) {
    val currentContext = LocalContext.current
    val authService = AuthorizationService(currentContext)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let {
                viewModel.exchangeToken(authService, it)
            } ?: run {
                Timber.e("Data not present")
                val ex = AuthorizationException.fromIntent(result.data!!)
                Timber.e(ex?.toJsonString())
            }
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Login")
        Button(
            onClick = {
                if (viewModel.isLoggedIn()) {
                    Timber.e("Logged in: ${viewModel.authStateManager.current.accessToken}")
                    Timber.e("Logged in: ${viewModel.authStateManager.current.accessTokenExpirationTime}")
                } else {
                    val authIntent =
                        authService.getAuthorizationRequestIntent(viewModel.authRequest)
                    launcher.launch(authIntent)
                }
            }
        ) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPageContentPreview() = WakaTimeAppTheme(true) {
//    LoginPageContent(LoginPageViewModel())
}
