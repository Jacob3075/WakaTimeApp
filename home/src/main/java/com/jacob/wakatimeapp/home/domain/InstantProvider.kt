package com.jacob.wakatimeapp.home.domain

import java.time.Instant
import javax.inject.Inject

fun interface InstantProvider {
    fun now(): Instant
}

class DefaultInstantProvider @Inject constructor() : InstantProvider {
    override fun now(): Instant = Instant.now()
}
