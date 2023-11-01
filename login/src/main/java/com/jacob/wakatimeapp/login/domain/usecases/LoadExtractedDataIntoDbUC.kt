package com.jacob.wakatimeapp.login.domain.usecases

import arrow.core.Either
import arrow.core.left
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.DatabaseError.UnknownError
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import timber.log.Timber

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

        return Either.catch {
            wakaTimeAppDB.insertExtractedData(extractedDataDTO)
        }.mapLeft {
            Timber.e("could not insert extracted data into db", it)
            UnknownError("could not insert extracted data into db", it)
        }
    }
}
