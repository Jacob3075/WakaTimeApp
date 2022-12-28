package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates

data class Language(override val name: String, override val time: Time) : SecondaryStat<Language> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

/**
 * Need to wrap data class in a sealed class to be able to use a data class with private constructor
 *
 * [Source](https://youtrack.jetbrains.com/issue/KT-11914/Confusing-data-class-copy-with-private-constructor)
 */
sealed class Languages : SecondaryStats<Language> {
    private data class LanguagesHolder(override val values: List<Language>) : Languages()

    override fun plus(other: SecondaryStats<Language>) = from(values + other.values)
    override fun copyStats(values: List<Language>) = from(values = values)

    companion object {
        fun from(values: List<Language>): Languages =
            LanguagesHolder(values.mergeDuplicates(::Language))

        val NONE = from(emptyList())
    }
}
