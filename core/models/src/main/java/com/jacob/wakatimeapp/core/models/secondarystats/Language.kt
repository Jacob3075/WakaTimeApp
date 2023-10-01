package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates
import kotlinx.serialization.Serializable

@Serializable
data class Language(override val name: String, override val time: Time) : SecondaryStat<Language> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

class Languages(values: List<Language>) : SecondaryStats<Language> {
    override val values: List<Language> = values.mergeDuplicates(::Language)

    override fun plus(other: SecondaryStats<Language>) = Languages(values + other.values)

    override fun copyStats(values: List<Language>) = Languages(values = values)

    companion object {
        val NONE = Languages(emptyList())
    }
}
