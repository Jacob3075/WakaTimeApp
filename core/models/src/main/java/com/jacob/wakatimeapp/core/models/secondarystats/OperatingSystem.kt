package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

data class OperatingSystem(override val name: String, override val time: Time) : SecondaryStat

data class OperatingSystems(override val values: List<OperatingSystem>) :
    SecondaryStats<OperatingSystem> {

    override fun plus(other: SecondaryStats<OperatingSystem>) =
        OperatingSystems(values + other.values)

    companion object {
        val NONE = OperatingSystems(emptyList())
    }
}
