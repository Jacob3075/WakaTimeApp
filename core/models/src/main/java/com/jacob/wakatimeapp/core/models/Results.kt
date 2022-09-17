package com.jacob.wakatimeapp.core.models

sealed class Result<out T : Any> {
    object Empty : Result<Nothing>()
    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val errorHolder: ErrorTypes) : Result<Nothing>() {

        fun getErrorMessage() =
            when (val errorType = errorHolder) {
                is ErrorTypes.NetworkError -> errorType.throwable.message ?: ""
            }
    }
}

sealed class ErrorTypes {
    data class NetworkError(val throwable: Throwable) : ErrorTypes()
}
