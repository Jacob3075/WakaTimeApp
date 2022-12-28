package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.topNAndCombineOthers

data class OperatingSystem(override val name: String, override val time: Time) :
    SecondaryStat<OperatingSystem> {
    override fun uncheckPlus(other: OperatingSystem) = copy(time = time + other.time)
}

data class OperatingSystems(override val values: List<OperatingSystem>) :
    SecondaryStats<OperatingSystem> {

    override val mostUsed = values.mostUsed()

    override fun plus(other: SecondaryStats<OperatingSystem>) =
        OperatingSystems(values + other.values)

    override fun topNAndCombineOthers(n: Int) = topNAndCombineOthers(
        n = n,
        creation = ::OperatingSystems,
    )

    companion object {
        val NONE = OperatingSystems(emptyList())

        fun createFrom(values: List<OperatingSystem>) =
            OperatingSystems(values.mergeDuplicates { OperatingSystem(it.key, it.value) })
    }
}
