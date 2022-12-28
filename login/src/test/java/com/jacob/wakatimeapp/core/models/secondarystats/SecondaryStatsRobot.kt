package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import io.kotest.matchers.shouldBe

internal class SecondaryStatsRobot {
    private lateinit var model: SecondaryStats<Language>

    fun createModel(list: List<Language>) = apply {
        model = Languages(list)
    }

    fun resultValuesShouldBe(emptyList: List<Language>) = apply {
        model.values shouldBe emptyList
    }

    fun addModel(languages: SecondaryStats<Language>) = apply {
        model += languages
    }

    fun getTopNShouldReturn(i: Int, languagesNoDuplicates: List<Language>) = apply {
        model.topNAndCombineOthers(i).values shouldBe languagesNoDuplicates
    }

    companion object {
        val LANGUAGES = listOf(
            Language("Kotlin", Time.fromDecimal(8.0f)),
            Language("Java", Time.fromDecimal(7.0f)),
            Language("Python", Time.fromDecimal(6.0f)),
        )

        val LANGUAGES_ALT = listOf(
            Language("something else 1", Time.fromDecimal(5.0f)),
            Language("something else 2", Time.fromDecimal(4.0f)),
        )

        val LANGUAGES_LARGE = LANGUAGES + LANGUAGES_ALT + listOf(
            Language("something else 3", Time.fromDecimal(3.0f)),
            Language("something else 4", Time.fromDecimal(2.0f)),
            Language("something else 5", Time.fromDecimal(1.0f)),
            Language("something else 6", Time.fromDecimal(0.5f)),
        )
    }
}
