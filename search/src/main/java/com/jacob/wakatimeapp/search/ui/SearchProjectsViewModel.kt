package com.jacob.wakatimeapp.search.ui

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.jacob.wakatimeapp.search.domain.usecases.GetAllProjectsUC
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Error
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loaded
import com.jacob.wakatimeapp.search.ui.SearchProjectsViewState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class SearchProjectsViewModel @Inject constructor(
    ioDispatcher: CoroutineContext = Dispatchers.IO,
    private val getAllProjectsUC: GetAllProjectsUC,
) : ViewModel() {
    private val _searchProjectsState =
        MutableStateFlow<SearchProjectsViewState>(Loading)
    val searchProjectsState = _searchProjectsState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            when (val eitherProjects = getAllProjectsUC()) {
                is Right -> _searchProjectsState.value = Loaded(eitherProjects.value)
                is Left -> _searchProjectsState.value = Error(eitherProjects.value)
            }
        }
    }

    fun updateSearchQuery(query: TextFieldValue) {
        when (val state = searchProjectsState.value) {
            is Loaded -> _searchProjectsState.value = state.copy(searchQuery = query)
            else -> Unit
        }
    }
}
