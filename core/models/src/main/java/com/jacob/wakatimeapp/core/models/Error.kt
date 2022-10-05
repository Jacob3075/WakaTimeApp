package com.jacob.wakatimeapp.core.models

import timber.log.Timber

sealed class Error : Exception() {
    abstract override val message: String

    sealed class DomainError : Error() {
        data class InvalidData(override val message: String) : DomainError()
        data class DataRangeTooLarge(override val message: String) : DomainError()
    }

    sealed class NetworkErrors : Error() {
        abstract val statusCode: Int

        data class NoConnection(override val message: String) : NetworkErrors() {
            override val statusCode = SERVICE_UNAVAILABLE
        }

        data class GenericError(override val message: String) : NetworkErrors() {
            override val statusCode: Int = -1
        }

        data class ClientError(override val message: String, override val statusCode: Int) :
            NetworkErrors()

        data class ServerError(override val message: String, override val statusCode: Int) :
            NetworkErrors()

        companion object {
            @Suppress("MagicNumber")
            fun create(message: String, code: Int? = null): NetworkErrors = when (code) {
                null -> GenericError(message)
                in 400..499 -> ClientError(message, code)
                SERVICE_UNAVAILABLE -> NoConnection(message)
                in 500..599 -> ServerError(message, code)
                else -> {
                    Timber.e("Unknown error code: $code, message: $message")
                    TODO("Handle this case")
                }
            }

            private const val SERVICE_UNAVAILABLE = 503
        }
    }
}
