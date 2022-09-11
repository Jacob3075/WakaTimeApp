package com.jacob.wakatimeapp.navigation

import androidx.navigation.NavController
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.jacob.wakatimeapp.navigation.destinations.HomePageDestination
import com.ramcosta.composedestinations.navigation.navigate

class ApplicationNavigator(private val navController: NavController) :
    HomePageNavigator by HomePageNavigatorImpl(navController),
    LoginPageNavigator by LoginPageNavigatorImpl(navController)

class LoginPageNavigatorImpl(private val navController: NavController) : LoginPageNavigator {
    override fun toHomePage() = navController.navigate(HomePageDestination)
}

class HomePageNavigatorImpl(private val navController: NavController) : HomePageNavigator {
    override fun toDetailsPage() = navController.navigate(HomePageDestination)
}
