package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.models.Error
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LoadExtractedDataIntoDbUC @Inject constructor() {
    suspend operator fun invoke(extractedDataDTO: ExtractedDataDTO): Either<Error, Unit> {
        TODO()

    }
}
