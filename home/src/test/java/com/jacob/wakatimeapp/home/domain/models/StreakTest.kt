package com.jacob.wakatimeapp.home.domain.models

import com.jacob.wakatimeapp.core.models.Streak
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

internal class StreakTest {
    @Test
    internal fun `when adding 2 streaks that do not overlap, then result should be zero`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 7),
            end = LocalDate(2022, 1, 10),
        )

        (streak1 + streak2) shouldBe Streak.ZERO
        (streak2 + streak1) shouldBe Streak.ZERO
    }

    @Test
    internal fun `when adding a streak that starts on the same day another ends, then streaks should be added`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 5),
            end = LocalDate(2022, 1, 10),
        )

        (streak1 + streak2) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )

        (streak2 + streak1) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
    }

    @Test
    internal fun `when adding a streak that starts on the day after another ends, then streaks should be added`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 6),
            end = LocalDate(2022, 1, 10),
        )

        (streak1 + streak2) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )

        (streak2 + streak1) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
    }

    @Test
    internal fun `when adding a streak that overlaps with another streak, then streaks should be added`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 10),
        )

        (streak1 + streak2) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )

        (streak2 + streak1) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
    }

    @Test
    internal fun `when adding a streak that is contained in another streak, then return containing streak`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 8),
        )

        (streak1 + streak2) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )

        (streak2 + streak1) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
    }

    @Test
    internal fun `when adding a streak that is contained in another streak and with same start or end dates, then return containing streak`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 4),
            end = LocalDate(2022, 1, 10),
        )

        (streak1 + streak2) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )

        (streak2 + streak1) shouldBe Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
    }

    @Test
    internal fun `when adding a streak to ZERO, then it should return the original streak`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
        val streak2 = Streak.ZERO

        (streak1 + streak2) shouldBe streak1
        (streak2 + streak1) shouldBe streak1
    }

    @Test
    internal fun `when 2 streaks with start of one over-lapping with end of other, then they can be combined`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 5),
            end = LocalDate(2022, 1, 10),
        )

        streak1.canBeCombinedWith(streak2).shouldBeTrue()
        streak2.canBeCombinedWith(streak1).shouldBeTrue()
    }

    @Test
    internal fun `when 2 streaks with start of one is one day apart from end of other, then they can be combined`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 6),
            end = LocalDate(2022, 1, 10),
        )

        streak1.canBeCombinedWith(streak2).shouldBeTrue()
        streak2.canBeCombinedWith(streak1).shouldBeTrue()
    }

    @Test
    internal fun `when a streak that overlaps with another streak, then streaks can be combined`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 5),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 10),
        )

        streak1.canBeCombinedWith(streak2).shouldBeTrue()
        streak2.canBeCombinedWith(streak1).shouldBeTrue()
    }

    @Test
    internal fun `when a streak that is contained in another streak, then streaks can be combined`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 3),
            end = LocalDate(2022, 1, 8),
        )

        streak1.canBeCombinedWith(streak2).shouldBeTrue()
        streak2.canBeCombinedWith(streak1).shouldBeTrue()
    }

    @Test
    internal fun `when 2 streaks are not consecutive, then they cannot be combined`() {
        val streak1 = Streak(
            start = LocalDate(2022, 1, 1),
            end = LocalDate(2022, 1, 10),
        )
        val streak2 = Streak(
            start = LocalDate(2022, 1, 13),
            end = LocalDate(2022, 1, 20),
        )

        streak1.canBeCombinedWith(streak2).shouldBeFalse()
        streak2.canBeCombinedWith(streak1).shouldBeFalse()
    }
}
