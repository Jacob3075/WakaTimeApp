package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.mergeDuplicates
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats.Companion.topNAndCombineOthers

data class Editor(override val name: String, override val time: Time) : SecondaryStat<Editor> {

    override fun uncheckPlus(other: Editor) = copy(time = time + other.time)
}

data class Editors(override val values: List<Editor>) : SecondaryStats<Editor> {

    override val mostUsed = values.mostUsed()

    override fun plus(other: SecondaryStats<Editor>) = Editors(values + other.values)

    override fun topNAndCombineOthers(n: Int) = topNAndCombineOthers(
        n = n,
        creation = ::Editors,
    )

    companion object {
        val NONE = Editors(emptyList())
        fun createFrom(values: List<Editor>) =
            Editors(values.mergeDuplicates { Editor(it.key, it.value) })
    }
}
