package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.topNAndCombineOthers

data class Language(override val name: String, override val time: Time) : SecondaryStat<Language> {
    override fun uncheckPlus(other: Language) = copy(time = time + other.time)
}

class Languages private constructor(override val values: List<Language>) :
    SecondaryStats<Language> {

    override val mostUsed = values.mostUsed()

    override fun plus(other: SecondaryStats<Language>) = Languages(values + other.values)

    override fun topNAndCombineOthers(n: Int) = topNAndCombineOthers(
        n = n,
        creation = ::Languages,
    )

    companion object {
        val NONE = Languages(emptyList())

        fun createFrom(values: List<Language>) =
            Languages(values.mergeDuplicates { Language(it.key, it.value) })
    }
}
