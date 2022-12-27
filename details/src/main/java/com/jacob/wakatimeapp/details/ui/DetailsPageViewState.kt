package com.jacob.wakatimeapp.details.ui

sealed class DetailsPageViewState {
    object Loading : DetailsPageViewState()

    data class Loaded(val detUiDetailsPageViewState: DetailsPageViewState) : DetailsPageViewState()

    data class Error(val error: com.jacob.wakatimeapp.core.models.Error) : DetailsPageViewState()
}
