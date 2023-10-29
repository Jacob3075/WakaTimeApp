package com.jacob.wakatimeapp.home.ui.extract

import com.jacob.wakatimeapp.core.models.Error as CoreModelsError
import com.jacob.wakatimeapp.home.domain.models.ExtractCreationProgress

internal sealed class ExtractPageViewState {
    data object Idle : ExtractPageViewState()

    data class CreatingExtract(val progress: Float) : ExtractPageViewState()

    data class ExtractCreated(val createdExtract: ExtractCreationProgress) : ExtractPageViewState()

    data object DownloadingExtract : ExtractPageViewState()

    data class Error(val error: CoreModelsError) : ExtractPageViewState()

    data object ExtractLoaded : ExtractPageViewState()
}
