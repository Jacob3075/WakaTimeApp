package com.jacob.wakatimeapp.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.jacob.wakatimeapp.core.ui.components.WtaAnimation
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Error
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loaded
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loading
import com.jacob.wakatimeapp.search.ui.components.ProjectsList
import com.jacob.wakatimeapp.search.ui.components.SearchBar

@Composable
fun SearchProjectsScreen(
    navigator: SearchProjectsNavigator,
    modifier: Modifier = Modifier,
) = SearchProjectsScreen(
    navigator = navigator,
    modifier = modifier,
    viewModel = hiltViewModel(),
)

@Composable
private fun SearchProjectsScreen(
    navigator: SearchProjectsNavigator,
    modifier: Modifier = Modifier,
    viewModel: SearchProjectsViewModel = hiltViewModel(),
) {
    val state by viewModel.searchProjectsState.collectAsState()

    when (val stateInstance = state) {
        is Loaded -> SearchProjectsLoaded(
            state = stateInstance,
            modifier = modifier,
            updateSearchQuery = viewModel::updateSearchQuery,
            onProjectItemClicked = { navigator.toProjectDetailsPage(it.name) },
        )

        Loading -> WtaAnimation(
            animation = MaterialTheme.assets.animations.randomLoadingAnimation,
            text = "Loading..",
        )

        is Error -> WtaAnimation(
            animation = MaterialTheme.assets.animations.randomErrorAnimation,
            text = stateInstance.error.message,
        )
    }
}

@Composable
private fun SearchProjectsLoaded(
    state: Loaded,
    modifier: Modifier = Modifier,
    updateSearchQuery: (TextFieldValue) -> Unit,
    onProjectItemClicked: (ProjectDetails) -> Unit,
) {
    Column(
        modifier = modifier.statusBarsPadding()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
            ),
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        SearchBar(
            value = state.searchQuery,
            onValueChange = updateSearchQuery,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        ProjectsList(projects = state.filteredProjects, onProjectItemClicked = onProjectItemClicked)
    }
}
