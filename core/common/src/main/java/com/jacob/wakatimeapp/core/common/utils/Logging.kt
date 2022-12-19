package com.jacob.wakatimeapp.core.common.utils // ktlint-disable filename

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.log() = onEach { Timber.d("flow:$it") }

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.log(tag: String) = onEach { Timber.d("flow:$tag: $it") }
