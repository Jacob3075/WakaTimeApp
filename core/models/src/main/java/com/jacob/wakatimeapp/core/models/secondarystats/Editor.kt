package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates

data class Editor(override val name: String, override val time: Time) : SecondaryStat<Editor> {

    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

class Editors(values: List<Editor>) : SecondaryStats<Editor> {
    override val values: List<Editor> = values.mergeDuplicates(::Editor)

    override fun plus(other: SecondaryStats<Editor>) = Editors(values + other.values)

    override fun copyStats(values: List<Editor>) = Editors(values)

    companion object {
        val NONE = Editors(emptyList())
    }
}
