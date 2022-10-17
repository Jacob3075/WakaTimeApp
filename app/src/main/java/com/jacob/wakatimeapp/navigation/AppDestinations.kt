package com.jacob.wakatimeapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.HomePageScreen
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.jacob.wakatimeapp.login.ui.LoginPageScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginPage(loginPageNavigator: LoginPageNavigator, scaffoldState: SnackbarHostState) =
    LoginPageScreen(loginPageNavigator = loginPageNavigator, snackbarHostState = scaffoldState)

@Composable
@Destination
fun HomePage(
    homePageNavigator: HomePageNavigator,
    scaffoldState: SnackbarHostState,
) = HomePageScreen(
    navigator = homePageNavigator,
    snackbarHostState = scaffoldState
)
