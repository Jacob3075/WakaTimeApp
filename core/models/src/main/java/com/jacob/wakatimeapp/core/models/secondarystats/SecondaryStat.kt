package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time

interface SecondaryStat {
    val name: String
    val time: Time
}

interface SecondaryStats<T : SecondaryStat> {
    val values: Iterable<T>

    operator fun plus(other: SecondaryStats<T>): SecondaryStats<T>
}
