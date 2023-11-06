package com.jacob.wakatimeapp.core.common.utils

import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface InstantProvider {
    val timeZone: TimeZone
    fun now(): Instant

    fun date(): LocalDate = now().toLocalDateTime(timeZone).date
}

class DefaultInstantProvider @Inject constructor() : InstantProvider {
    override val timeZone = TimeZone.currentSystemDefault()

    override fun now(): Instant = Clock.System.now()
}
