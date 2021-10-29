package com.jacob.wakatimeapp.common.models

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val errorHolder: ErrorTypes) : Result<Nothing>()
}

sealed class ErrorTypes {
    data class NetworkError(val throwable: Throwable) : ErrorTypes()
}
