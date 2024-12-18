package com.jacob.wakatimeapp.navigation

import androidx.navigation.navOptions
import com.jacob.wakatimeapp.details.ui.DetailsPageNavigator
import com.jacob.wakatimeapp.details.ui.destinations.DetailsPageDestination
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.destinations.HomePageDestination
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.jacob.wakatimeapp.login.ui.destinations.ExtractUserDataPageDestination
import com.jacob.wakatimeapp.login.ui.destinations.LoginPageDestination
import com.jacob.wakatimeapp.login.ui.extract.ExtractUserDataNavigator
import com.jacob.wakatimeapp.search.ui.SearchProjectsNavigator
import com.jacob.wakatimeapp.search.ui.destinations.SearchProjectsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ApplicationNavigator(private val navigator: DestinationsNavigator) :
    HomePageNavigator by HomePageNavigatorImpl(navigator),
    LoginPageNavigator by LoginPageNavigatorImpl(navigator),
    DetailsPageNavigator by DetailsPageNavigatorImpl(navigator),
    ExtractUserDataNavigator by ExtractUserDataNavigatorImpl(navigator),
    SearchProjectsNavigator {

    override fun toProjectDetailsPage(projectName: String) =
        navigator.navigate(DetailsPageDestination(projectName = projectName))
}

class ExtractUserDataNavigatorImpl(private val navigator: DestinationsNavigator) :
    ExtractUserDataNavigator {
    override fun toHomePageFromExtractUserData() = navigator.navigate(
        HomePageDestination,
        navOptions = navOptions { popUpTo(ExtractUserDataPageDestination.route) { inclusive = true } },
    )
}

class LoginPageNavigatorImpl(private val navigator: DestinationsNavigator) : LoginPageNavigator {
    override fun toExtractUserDataPage() = navigator.navigate(
        ExtractUserDataPageDestination,
        navOptions = navOptions { popUpTo(LoginPageDestination.route) { inclusive = true } },
    )

    override fun toHomePage() = navigator.navigate(
        HomePageDestination,
        navOptions = navOptions { popUpTo(HomePageDestination.route) { inclusive = true } },
    )
}

class HomePageNavigatorImpl(private val navigator: DestinationsNavigator) : HomePageNavigator {
    override fun toProjectDetailsPage(projectName: String) =
        error("Should not be called, implemented in ApplicationNavigator should be used")

    override fun toSearchPage() = navigator.navigate(SearchProjectsDestination)
}

class DetailsPageNavigatorImpl(private val navigator: DestinationsNavigator) :
    DetailsPageNavigator
