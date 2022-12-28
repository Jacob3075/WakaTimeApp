package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

data class Language(override val name: String, override val time: Time) : SecondaryStat<Language> {
    constructor(entry: Map.Entry<String, Time>) : this(entry.key, entry.value)

    override fun uncheckedPlus(other: Language) = copy(time = time + other.time)
}

class Languages(values: List<Language>) :
    SecondaryStats<Language>(values.mergeDuplicates(::Language)) {

    override fun plus(other: SecondaryStats<Language>) = Languages(values + other.values)

    override fun topNAndCombineOthers(n: Int) = topNAndCombineOthers(
        n = n,
        creation = ::Languages,
    )

    companion object {
        val NONE = Languages(emptyList())
    }
}
