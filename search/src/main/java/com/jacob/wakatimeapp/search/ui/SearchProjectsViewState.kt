package com.jacob.wakatimeapp.search.ui

import androidx.compose.ui.text.input.TextFieldValue
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails

sealed class SearchProjectsViewState {
    object Loading : SearchProjectsViewState()
    data class Loaded(
        val projects: List<ProjectDetails>,
        val searchQuery: TextFieldValue = TextFieldValue(""),
    ) : SearchProjectsViewState() {
        val filteredProjects = projects.filter {
            it.name.contains(
                searchQuery.text.trim(),
                ignoreCase = !searchQuery.text.any(Char::isUpperCase),
            )
        }
    }

    data class Error(val error: com.jacob.wakatimeapp.core.models.Error) : SearchProjectsViewState()
}
