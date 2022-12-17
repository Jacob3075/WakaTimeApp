package com.jacob.wakatimeapp.search.ui

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails

sealed class SearchProjectsViewState {
    object Loading : SearchProjectsViewState()
    data class Loaded(val projects: List<ProjectDetails>) : SearchProjectsViewState()
    data class Error(val error: CoreModelsError) : SearchProjectsViewState()
}
