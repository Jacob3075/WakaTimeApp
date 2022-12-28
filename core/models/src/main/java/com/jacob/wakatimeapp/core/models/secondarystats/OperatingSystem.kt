package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

data class OperatingSystem(override val name: String, override val time: Time) :
    SecondaryStat<OperatingSystem> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

class OperatingSystems(values: List<OperatingSystem>) :
    SecondaryStats<OperatingSystem>(values.mergeDuplicates(::OperatingSystem)) {

    override fun plus(other: SecondaryStats<OperatingSystem>) =
        OperatingSystems(values + other.values)

    override fun copy(values: List<OperatingSystem>) = OperatingSystems(values)

    companion object {
        val NONE = OperatingSystems(emptyList())
    }
}
