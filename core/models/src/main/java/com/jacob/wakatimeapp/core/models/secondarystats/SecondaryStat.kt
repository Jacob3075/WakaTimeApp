package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

/**
 * Need to add generics to [SecondaryStat] to be able to use [uncheckPlus] function without casting
 *
 * [Source](https://stackoverflow.com/a/47796513/13181948)
 */
interface SecondaryStat<T : SecondaryStat<T>> {
    val name: String
    val time: Time
    fun uncheckPlus(other: T): T
}

interface SecondaryStats<T : SecondaryStat<T>> {
    val values: List<T>
    val mostUsed: T

    operator fun plus(other: SecondaryStats<T>): SecondaryStats<T>
    fun topNAndCombineOthers(n: Int): SecondaryStats<T>

    fun List<T>.mostUsed() = this.maxBy { it.time.decimal }

    companion object {
        @JvmStatic
        fun <T : SecondaryStat<T>> Iterable<T>.mergeDuplicates(
            param: (Map.Entry<String, Time>) -> T,
        ) = groupingBy(SecondaryStat<T>::name)
            .fold(Time.ZERO, Time::plus)
            .map(param)

        @JvmStatic
        fun <T : SecondaryStat<T>> SecondaryStats<T>.topNAndCombineOthers(
            n: Int,
            creation: (List<T>) -> SecondaryStats<T>,
        ): SecondaryStats<T> {
            if (values.size <= n) return this

            val topN = values.subList(0, n + 1)
            val others = values.drop(n)
                .reduce(SecondaryStat<T>::uncheckPlus)

            return creation(topN + others)
        }
    }
}

private operator fun <T : SecondaryStat<T>> Time.plus(element: SecondaryStat<T>) =
    this + element.time
