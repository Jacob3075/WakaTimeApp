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
import com.jacob.wakatimeapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientSecretPost
import timber.log.Timber


@AndroidEntryPoint
class LoginPage : Fragment() {
    private val viewModel by viewModels<LoginPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Timber.e("Data present")
            it.data?.let { intent ->
                val resp = AuthorizationResponse.fromIntent(intent)!!
                authService.performTokenRequest(
                    resp.createTokenExchangeRequest(),
                    ClientSecretPost(Constants.clientSecret)
                ) { tokenResponse, authorizationException ->
                    tokenResponse?.let {
                        Timber.e(tokenResponse.jsonSerializeString())
                    } ?: run {
                        Timber.e(authorizationException)
                    }
                }
                resp
            } ?: run {
                Timber.e("Data not present")
                val ex = AuthorizationException.fromIntent(it.data!!)
                Timber.e(ex?.toJsonString())
            }
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Login")
        Button(onClick = {
            val authIntent = authService.getAuthorizationRequestIntent(viewModel.authRequest)
            launcher.launch(authIntent)
        }) {
            Text(text = "Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPageContentPreview() = WakaTimeAppTheme(true) {
    LoginPageContent(LoginPageViewModel())
}
/*
*
*
                val tokenRequest = TokenRequest.Builder(
                    AuthorizationServiceConfiguration(
                        Uri.parse("https://wakatime.com/oauth/authorize"),
                        Uri.parse("https://wakatime.com/oauth/token")
                    ),
                    "UKQ9xvpSflsXL1dS7dgMa6h3"
                ).setRedirectUri(Uri.parse("wakatimeapp://oauth2redirect"))
                    .setGrantType("authorization_code")
                    .setAuthorizationCode(resp.authorizationCode)
                    .build()
*
*
*
* */