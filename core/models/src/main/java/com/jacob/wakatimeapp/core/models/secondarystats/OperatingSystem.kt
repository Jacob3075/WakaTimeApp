package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates

data class OperatingSystem(override val name: String, override val time: Time) :
    SecondaryStat<OperatingSystem> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

sealed class OperatingSystems : SecondaryStats<OperatingSystem> {
    private data class OperatingSystemsHolder(override val values: List<OperatingSystem>) :
        OperatingSystems()

    override fun plus(other: SecondaryStats<OperatingSystem>) =
        from(values + other.values)

    override fun copyStats(values: List<OperatingSystem>) = from(values)

    companion object {
        fun from(values: List<OperatingSystem>): OperatingSystems =
            OperatingSystemsHolder(values.mergeDuplicates(::OperatingSystem))

        val NONE = from(emptyList())
    }
}
