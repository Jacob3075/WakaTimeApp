package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

data class Editor(override val name: String, override val time: Time) : SecondaryStat

data class Editors(override val values: List<Editor>) : SecondaryStats<Editor> {

    override fun plus(other: SecondaryStats<Editor>) = Editors(values + other.values)

    companion object {
        val NONE = Editors(emptyList())
    }
}
