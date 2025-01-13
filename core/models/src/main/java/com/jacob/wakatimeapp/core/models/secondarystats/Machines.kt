package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates
import kotlinx.serialization.Serializable

@Serializable
data class Machine(override val name: String, override val time: Time) :
    SecondaryStat<Machine> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun copyStat(name: String, time: Time) = copy(name = name, time = time)
}

class Machines(values: List<Machine>) : SecondaryStats<Machine> {
    override val values: List<Machine> = values.mergeDuplicates(::Machine)

    override fun plus(other: SecondaryStats<Machine>) =
        Machines(values + other.values)

    override fun copyStats(values: List<Machine>) = Machines(values)

    companion object {
        val NONE = Machines(emptyList())
    }
}
