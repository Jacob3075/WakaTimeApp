package com.jacob.wakatimeapp.core.models.secondarystats

import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStatsRobot.Companion.LANGUAGES
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStatsRobot.Companion.LANGUAGES_ALT
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStatsRobot.Companion.LANGUAGES_LARGE
import org.junit.jupiter.api.Test

internal class SecondaryStatsTest {
    private val modelRobot = SecondaryStatsRobot()

    @Test
    internal fun `when creating a secondary stats with an empty list, then creation should not fail`() {
        modelRobot.createModel(emptyList())
            .resultValuesShouldBe(emptyList())
    }

    @Test
    internal fun `when creating a model without multiple stats with same name, then values should not be merged`() {
        modelRobot.createModel(LANGUAGES)
            .resultValuesShouldBe(LANGUAGES)
    }

    @Test
    internal fun `when creating model with multiple stats with same name, then stats with same name should be merged`() {
        modelRobot.createModel(
            LANGUAGES + LANGUAGES + LANGUAGES +
                LANGUAGES_ALT,
        ).resultValuesShouldBe(
            LANGUAGES.map { it.copy(time = it.time * 3) } +
                LANGUAGES_ALT,
        )
    }

    @Test
    internal fun `when adding 2 models without duplicate stats, then values should be added without merging`() {
        modelRobot.createModel(LANGUAGES)
            .addModel(Languages(LANGUAGES_ALT))
            .resultValuesShouldBe(LANGUAGES + LANGUAGES_ALT)
    }

    @Test
    internal fun `when adding 2 models with duplicate stats, then values should be merged and added`() {
        modelRobot.createModel(LANGUAGES)
            .addModel(Languages(LANGUAGES + LANGUAGES_ALT))
            .resultValuesShouldBe(
                LANGUAGES.map { it.copy(time = it.time * 2) } + LANGUAGES_ALT,
            )
    }

    @Test
    internal fun `when getting the top n stats from a model with less than n stats, then return those values without modifying`() {
        modelRobot.createModel(LANGUAGES)
            .getTopNShouldReturn(5, LANGUAGES)
    }

    @Test
    internal fun `when getting the top n stats from a model with n stats, then return those values without modifying`() {
        modelRobot.createModel(LANGUAGES)
            .getTopNShouldReturn(3, LANGUAGES)
    }

    @Test
    internal fun `when getting the top n stats from a model with more than n stats, then return top n and combine others`() {
        modelRobot.createModel(LANGUAGES_LARGE)
            .getTopNShouldReturn(
                i = 3,
                languagesNoDuplicates = LANGUAGES_LARGE.subList(0, 3) + listOf(
                    Language("Others", Time.fromDecimal(15.5f)),
                ),
            )
    }
}

private operator fun Time.times(i: Int) = Time.fromDecimal(this.decimal * i)
