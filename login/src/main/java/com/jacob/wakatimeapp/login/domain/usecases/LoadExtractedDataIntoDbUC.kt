package com.jacob.wakatimeapp.login.domain.usecases

import arrow.core.Either
import arrow.core.left
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.models.Error
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Singleton
internal class LoadExtractedDataIntoDbUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
    private val json: Json,
) {
    suspend operator fun invoke(bytes: ByteArray?): Either<Error, Unit> {
        bytes ?: run {
            return Error.UnknownError("Could not load data from extract").left()
        }

        val extractedDataDTO = json.decodeFromString<ExtractedDataDTO>(String(bytes))

        return wakaTimeAppDB.updateDbWithNewData(extractedDataDTO)
    }
}
