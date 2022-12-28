package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates

data class Editor(override val name: String, override val time: Time) : SecondaryStat<Editor> {

    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

sealed class Editors : SecondaryStats<Editor> {
    private data class EditorsHolder(override val values: List<Editor>) : Editors()

    override fun plus(other: SecondaryStats<Editor>) = from(values + other.values)

    override fun copyStats(values: List<Editor>) = from(values)

    companion object {
        fun from(values: List<Editor>): Editors =
            EditorsHolder(values.mergeDuplicates(::Editor))

        val NONE = from(emptyList())
    }
}
