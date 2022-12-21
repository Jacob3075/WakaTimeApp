package com.jacob.wakatimeapp.navigation

import com.jacob.wakatimeapp.details.ui.DetailsPageNavigator
import com.jacob.wakatimeapp.home.ui.HomePageNavigator
import com.jacob.wakatimeapp.login.ui.LoginPageNavigator
import com.jacob.wakatimeapp.navigation.destinations.HomePageDestination
import com.jacob.wakatimeapp.navigation.destinations.SearchProjectsDestination
import com.jacob.wakatimeapp.search.ui.SearchProjectsNavigator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ApplicationNavigator(private val navigator: DestinationsNavigator) :
    HomePageNavigator by HomePageNavigatorImpl(navigator),
    LoginPageNavigator by LoginPageNavigatorImpl(navigator),
    SearchProjectsNavigator by SearchPageNavigatorImpl(navigator),
    DetailsPageNavigator by DetailsPageNavigatorImpl(navigator)

class LoginPageNavigatorImpl(private val navigator: DestinationsNavigator) : LoginPageNavigator {
    override fun toHomePage() = navigator.navigate(HomePageDestination)
}

class HomePageNavigatorImpl(private val navigator: DestinationsNavigator) : HomePageNavigator {
    override fun toDetailsPage() = navigator.navigate(HomePageDestination)
    override fun toSearchPage() = navigator.navigate(SearchProjectsDestination)
}

class SearchPageNavigatorImpl(private val navigator: DestinationsNavigator) :
    SearchProjectsNavigator

class DetailsPageNavigatorImpl(private val navigator: DestinationsNavigator) :
    DetailsPageNavigator
