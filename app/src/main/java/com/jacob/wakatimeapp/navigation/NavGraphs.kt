package com.jacob.wakatimeapp.navigation

import com.jacob.wakatimeapp.details.ui.destinations.DetailsPageDestination
import com.jacob.wakatimeapp.home.ui.destinations.ExtractUserDataPageDestination
import com.jacob.wakatimeapp.home.ui.destinations.HomePageDestination
import com.jacob.wakatimeapp.login.ui.destinations.LoginPageDestination
import com.jacob.wakatimeapp.search.ui.destinations.SearchProjectsDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {
    val root = object : NavGraphSpec {
        override val route = "root"

        override val startRoute = LoginPageDestination

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            LoginPageDestination,
            HomePageDestination,
            SearchProjectsDestination,
            DetailsPageDestination,
            ExtractUserDataPageDestination,
        ).associateBy { it.route }
    }
}
