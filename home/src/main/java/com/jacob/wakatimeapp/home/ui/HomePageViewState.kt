package com.jacob.wakatimeapp.home.ui

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData

sealed class HomePageViewState {
    data class Loaded(
        val contentData: HomePageUiData,
    ) : HomePageViewState()

    data class Error(val error: CoreModelsError) : HomePageViewState()

    object Loading : HomePageViewState()
}
