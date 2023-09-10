package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError

internal sealed class ExtractPageViewState {
    data object Idle : ExtractPageViewState()

    data class CreatingExtract(val progress: Float) : ExtractPageViewState()

    data object ExtractCreated : ExtractPageViewState()

    data class DownloadingExtract(val progress: Float) : ExtractPageViewState()

    data class Error(val error: CoreModelsError) : ExtractPageViewState()
}
