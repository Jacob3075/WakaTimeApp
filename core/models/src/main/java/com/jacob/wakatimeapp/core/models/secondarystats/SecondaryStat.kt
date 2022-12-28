package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

/**
 * Need to add generics to [SecondaryStat] to be able to use [uncheckedPlus] function without casting
 *
 * [Source](https://stackoverflow.com/a/47796513/13181948)
 */
interface SecondaryStat<T : SecondaryStat<T>> {
    val name: String
    val time: Time
    fun uncheckedPlus(other: T): T
}

abstract class SecondaryStats<T : SecondaryStat<T>>(val values: List<T>) {
    val mostUsed get() = values.maxBy { it.time.totalSeconds }

    abstract operator fun plus(other: SecondaryStats<T>): SecondaryStats<T>

    abstract fun topNAndCombineOthers(n: Int): SecondaryStats<T>

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
                .reduce(SecondaryStat<T>::uncheckedPlus)

            return creation(topN + others)
        }
    }
}

private operator fun <T : SecondaryStat<T>> Time.plus(element: SecondaryStat<T>) =
    this + element.time
