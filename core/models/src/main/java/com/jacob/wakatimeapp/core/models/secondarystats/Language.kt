package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

data class Language(override val name: String, override val time: Time) : SecondaryStat

data class Languages(override val values: List<Language>) : SecondaryStats<Language> {

    companion object {
        val NONE = Languages(emptyList())
    }

    override fun plus(other: SecondaryStats<Language>) = Languages(values + other.values)
}
