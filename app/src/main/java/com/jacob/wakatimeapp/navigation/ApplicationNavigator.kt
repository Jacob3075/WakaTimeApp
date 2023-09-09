package com.jacob.wakatimeapp.navigation

import com.jacob.wakatimeapp.details.ui.DetailsPageNavigator
import com.jacob.wakatimeapp.details.ui.destinations.DetailsPageDestination
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.home.ui.destinations.ExtractUserDataPageDestination
import com.jacob.wakatimeapp.home.ui.destinations.HomePageDestination
import com.jacob.wakatimeapp.home.ui.extract.ExtractUserDataNavigator
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
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
    override fun toHomePage() = navigator.navigate(HomePageDestination)
}

class LoginPageNavigatorImpl(private val navigator: DestinationsNavigator) : LoginPageNavigator {
    override fun toExtractUserDataPage() = navigator.navigate(ExtractUserDataPageDestination)
}

class HomePageNavigatorImpl(private val navigator: DestinationsNavigator) : HomePageNavigator {
    override fun toProjectDetailsPage(projectName: String) =
        error("Should not be called, implemented in ApplicationNavigator should be used")

    override fun toSearchPage() = navigator.navigate(SearchProjectsDestination)
}

class DetailsPageNavigatorImpl(private val navigator: DestinationsNavigator) :
    DetailsPageNavigator
