package com.jacob.wakatimeapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.details.ui.DetailsPageNavArgs
import com.jacob.wakatimeapp.details.ui.DetailsPageNavigator
import com.jacob.wakatimeapp.details.ui.DetailsPageScreen
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.HomePageScreen
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.jacob.wakatimeapp.login.ui.LoginPageScreen
import com.jacob.wakatimeapp.search.ui.SearchProjectsNavigator
import com.jacob.wakatimeapp.search.ui.SearchProjectsScreen
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
    snackbarHostState = scaffoldState,
)

@Composable
@Destination
fun SearchProjects(searchProjectsNavigator: SearchProjectsNavigator) =
    SearchProjectsScreen(navigator = searchProjectsNavigator)

@Composable
@Destination(navArgsDelegate = DetailsPageNavArgs::class)
fun DetailsPage(navigator: DetailsPageNavigator) = DetailsPageScreen(navigator = navigator)
