package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

/**
 * Need to add generics to [SecondaryStat] to be able to still have correct subtypes
 *
 * [Source](https://stackoverflow.com/a/47796513/13181948)
 */
interface SecondaryStat<T : SecondaryStat<T>> {
    val name: String
    val time: Time

    fun copyStat(name: String = this.name, time: Time = this.time): T
}

interface SecondaryStats<T : SecondaryStat<T>> {
    val values: List<T>
    val mostUsed get() = values.maxByOrNull { it.time.totalSeconds }

    operator fun plus(other: SecondaryStats<T>): SecondaryStats<T>

    fun copyStats(values: List<T> = this.values): SecondaryStats<T>

    fun topNAndCombineOthers(n: Int): SecondaryStats<T> {
        if (values.size <= n) return this

        val topN = values.subList(0, n)
        val others = values.drop(n)
            .reduce(::reducer)
            .copyStat(name = "Others")

        return copyStats(values = topN + others)
    }

    private fun reducer(acc: T, other: T) = acc.copyStat(time = acc.time + other.time)

    companion object {
        @JvmStatic
        fun <T : SecondaryStat<T>> Iterable<T>.mergeDuplicates(
            param: (Map.Entry<String, Time>) -> T,
        ) = groupingBy(SecondaryStat<T>::name)
            .fold(Time.ZERO, Time::plus)
            .map(param)
    }
}

private operator fun <T : SecondaryStat<T>> Time.plus(element: SecondaryStat<T>) =
    this + element.time
