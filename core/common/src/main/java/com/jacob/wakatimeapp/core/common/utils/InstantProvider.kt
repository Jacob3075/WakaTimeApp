package com.jacob.wakatimeapp.core.common.utils

import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

interface InstantProvider {
    val timeZone: TimeZone
    fun now(): Instant
}

class DefaultInstantProvider @Inject constructor() : InstantProvider {
    override val timeZone = TimeZone.currentSystemDefault()

    override fun now(): Instant = Clock.System.now()
}
