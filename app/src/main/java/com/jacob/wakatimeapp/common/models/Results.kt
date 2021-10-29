package com.jacob.wakatimeapp.common.models

import com.jacob.wakatimeapp.home.domain.models.DailyStats

sealed class Result<out T : Any> {
    object Empty : Result<DailyStats>()
    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val errorHolder: ErrorTypes) : Result<Nothing>()
}

sealed class ErrorTypes {
    data class NetworkError(val throwable: Throwable) : ErrorTypes()
}
