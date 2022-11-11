package com.jacob.wakatimeapp.home.domain.models

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

internal class StreakRangeTest {
    @Test
    internal fun `when adding 2 streaks that do not overlap, then result should be zero`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 7),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange1 + streakRange2) shouldBe StreakRange.ZERO
        (streakRange2 + streakRange1) shouldBe StreakRange.ZERO
    }

    @Test
    internal fun `when adding a streak that starts on the same day another ends, then streaks should be added`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 5),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange1 + streakRange2) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange2 + streakRange1) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
    }

    @Test
    internal fun `when adding a streak that starts on the day after another ends, then streaks should be added`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 6),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange1 + streakRange2) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange2 + streakRange1) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
    }

    @Test
    internal fun `when adding a streak that overlaps with another streak, then streaks should be added`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange1 + streakRange2) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange2 + streakRange1) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
    }

    @Test
    internal fun `when adding a streak that is contained in another streak, then return containing streak`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 8)
        )

        (streakRange1 + streakRange2) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )

        (streakRange2 + streakRange1) shouldBe StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
    }

    @Test
    internal fun `when adding a streak to ZERO, then it should return the original streak`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
        val streakRange2 = StreakRange.ZERO

        (streakRange1 + streakRange2) shouldBe streakRange1
        (streakRange2 + streakRange1) shouldBe streakRange1
    }

    @Test
    internal fun `when 2 streaks with start of one over-lapping with end of other, then they can be combined`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 5),
            end = LocalDate(2022, 1, 10)
        )

        streakRange1.canBeCombinedWith(streakRange2).shouldBeTrue()
        streakRange2.canBeCombinedWith(streakRange1).shouldBeTrue()
    }

    @Test
    internal fun `when 2 streaks with start of one is one day apart from end of other, then they can be combined`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 6),
            end = LocalDate(2022, 1, 10)
        )

        streakRange1.canBeCombinedWith(streakRange2).shouldBeTrue()
        streakRange2.canBeCombinedWith(streakRange1).shouldBeTrue()
    }

    @Test
    internal fun `when a streak that overlaps with another streak, then streaks can be combined`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 10)
        )

        streakRange1.canBeCombinedWith(streakRange2).shouldBeTrue()
        streakRange2.canBeCombinedWith(streakRange1).shouldBeTrue()
    }

    @Test
    internal fun `when a streak that is contained in another streak, then streaks can be combined`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 8)
        )

        streakRange1.canBeCombinedWith(streakRange2).shouldBeTrue()
        streakRange2.canBeCombinedWith(streakRange1).shouldBeTrue()
    }

    @Test
    internal fun `when 2 streaks are not consecutive, then they cannot be combined`() {
        val streakRange1 = StreakRange(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10)
        )
        val streakRange2 = StreakRange(
            start = LocalDate(2022, 1, 13),
            end = LocalDate(2022, 1, 20)
        )

        streakRange1.canBeCombinedWith(streakRange2).shouldBeFalse()
        streakRange2.canBeCombinedWith(streakRange1).shouldBeFalse()
    }
}
