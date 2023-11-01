package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.right
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.home.domain.models.Streak
import com.jacob.wakatimeapp.home.domain.usecases.RecalculateLatestStreakUCRobot.Companion.createDailyStats
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.Test

internal class RecalculateLatestStreakUCTest {
    private val robot = RecalculateLatestStreakUCRobot()

    @Test
    internal fun `when called with duration and there is no valid streak, then return empty streak`() =
        runTest {
            val start = "2022-03-01".toLocalDate()
            val end = "2022-02-01".toLocalDate()

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(
                    values = createDailyStats(30, emptyList(), end),
                ).right(),
            ).calculate(
                start = start,
                DatePeriod(months = 1),
            ).resultShouldBe(Streak.ZERO.right()).verifyGetDataForRange(start, end)
        }

    @Test
    internal fun `when there are non-continuous stats that include end of the duration, then return latest streak`() =
        runTest {
            val days = listOf(0, 1, 2, 5, 6, 7, 8, 11, 12, 13, 14)
            val start = LocalDate(2022, 3, 31)
            val end = LocalDate(2022, 3, 17)

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(values = createDailyStats(size = 15, days, end)).right(),
            ).calculate(
                start = start,
                DatePeriod(days = 14),
            ).resultShouldBe(
                Streak(
                    start = LocalDate(2022, 3, 28),
                    end = start,
                ).right(),
            ).verifyGetDataForRange(start, end)
        }

    @Test
    internal fun `when there are none continuous stats that do not include end of the duration, then return empty streak`() =
        runTest {
            val days = listOf(0, 1, 2, 5, 6, 7, 8, 10, 11, 12)
            val start = "2022-03-31".toLocalDate()
            val end = "2022-03-17".toLocalDate()

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(values = createDailyStats(14, days, end)).right(),
            ).calculate(
                start = start,
                DatePeriod(days = 14),
            ).resultShouldBe(Streak.ZERO.right()).verifyGetDataForRange(start, end)
        }

    @Test
    internal fun `when entire duration is part of the streak, then should itself again with next duration`() =
        runTest {
            val start = "2022-03-01".toLocalDate()
            val end = "2022-02-01".toLocalDate()
            val count = end.daysUntil(start) + 1
            val days = List(count) { it }
            val secondStart = end.minus(1, DateTimeUnit.DAY)
            val secondEnd = secondStart.minus(1, DateTimeUnit.MONTH)

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(
                    values = createDailyStats(count, days, end),
                ).right(),
            ).mockGetDataForRange(
                secondStart,
                secondEnd,
                DailyStatsAggregate(
                    values = createDailyStats(count, emptyList(), end),
                ).right(),
            ).calculate(
                start = start,
                DatePeriod(months = 1),
            ).verifyGetDataForRange(start, end).verifyGetDataForRange(
                secondStart,
                secondEnd,
            )
        }

    @Test
    internal fun `when entire duration is part of the streak and next duration has a few days that's part of the streak, then streak should be sum of the 2`() =
        runTest {
            val start = LocalDate(2022, 3, 31)
            val end = LocalDate(2022, 3, 17)
            val count = end.daysUntil(start) + 1
            val days = List(count) { it }
            val secondStart = LocalDate(2022, 3, 16)
            val secondEnd = LocalDate(2022, 3, 2)
            val days2 = listOf(0, 1, 2, 3, 8, 9, 11, 12, 13, 14)

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(values = createDailyStats(count, days, end)).right(),
            ).mockGetDataForRange(
                secondStart,
                secondEnd,
                DailyStatsAggregate(values = createDailyStats(count, days2, secondEnd)).right(),
            ).calculate(
                start = start,
                DatePeriod(days = 14),
            ).resultShouldBe(
                Streak(
                    start = "2022-03-13".toLocalDate(),
                    end = "2022-03-31".toLocalDate(),
                ).right(),
            )
        }

    @Test
    internal fun `when entire duration is part of the streak and next duration has a few days that's not part of the streak, then streak should not be added`() =
        runTest {
            val start = LocalDate(2022, 3, 31)
            val end = LocalDate(2022, 3, 17)
            val count = end.daysUntil(start) + 1
            val days = List(count) { it }
            val secondStart = LocalDate(2022, 3, 16)
            val secondEnd = LocalDate(2022, 3, 2)
            val days2 = listOf(0, 1, 2, 3, 8, 9, 11, 12, 13)

            robot.buildService().mockGetDataForRange(
                start,
                end,
                DailyStatsAggregate(values = createDailyStats(count, days, end)).right(),
            ).mockGetDataForRange(
                secondStart,
                secondEnd,
                DailyStatsAggregate(values = createDailyStats(count, days2, secondEnd)).right(),
            ).calculate(
                start = start,
                DatePeriod(days = 14),
            ).resultShouldBe(
                Streak(
                    start = "2022-03-17".toLocalDate(),
                    end = "2022-03-31".toLocalDate(),
                ).right(),
            )
        }

    @Test
    internal fun `when streak lasts for multiple months, then result should be sum of all the streaks`() =
        runTest {
            val start1 = LocalDate(2022, 4, 30)
            val end1 = start1.minus(1, DateTimeUnit.MONTH)
            val count1 = end1.daysUntil(start1) + 1
            val days1 = List(count1) { it }

            val start2 = end1.minus(1, DateTimeUnit.DAY)
            val end2 = start2.minus(1, DateTimeUnit.MONTH)
            val count2 = end2.daysUntil(start2) + 1
            val days2 = List(count2) { it }

            val start3 = end2.minus(1, DateTimeUnit.DAY)
            val end3 = start3.minus(1, DateTimeUnit.MONTH)
            val count3 = end3.daysUntil(start3) + 1
            val days3 = List(count3) { it }

            val start4 = end3.minus(1, DateTimeUnit.DAY)
            val end4 = start4.minus(1, DateTimeUnit.MONTH)
            val count4 = end4.daysUntil(start4) + 1
            val days4 = List(count4) { it }

            val start5 = end4.minus(1, DateTimeUnit.DAY)
            val end5 = start5.minus(1, DateTimeUnit.MONTH)
            val count5 = end5.daysUntil(start5) + 1
            val days5 = List(count5) { it }

            val start6 = end5.minus(1, DateTimeUnit.DAY)
            val end6 = start6.minus(1, DateTimeUnit.MONTH)
            val count6 = end6.daysUntil(start6) + 1
            val days6 = List(count6) { it }

            val start7 = end6.minus(1, DateTimeUnit.DAY)
            val end7 = start7.minus(1, DateTimeUnit.MONTH)
            val count7 = end7.daysUntil(start7) + 1
            val days7 = emptyList<Int>()

            robot.buildService().mockGetDataForRange(
                start1,
                end1,
                DailyStatsAggregate(values = createDailyStats(count1, days1, end1)).right(),
            ).mockGetDataForRange(
                start2,
                end2,
                DailyStatsAggregate(values = createDailyStats(count2, days2, end2)).right(),
            ).mockGetDataForRange(
                start3,
                end3,
                DailyStatsAggregate(values = createDailyStats(count3, days3, end3)).right(),
            ).mockGetDataForRange(
                start4,
                end4,
                DailyStatsAggregate(values = createDailyStats(count4, days4, end4)).right(),
            ).mockGetDataForRange(
                start5,
                end5,
                DailyStatsAggregate(values = createDailyStats(count5, days5, end5)).right(),
            ).mockGetDataForRange(
                start6,
                end6,
                DailyStatsAggregate(values = createDailyStats(count6, days6, end6)).right(),
            ).mockGetDataForRange(
                start7,
                end7,
                DailyStatsAggregate(values = createDailyStats(count7, days7, end7)).right(),
            ).calculate(
                start = start1,
                DatePeriod(months = 1),
            ).resultShouldBe(
                Streak(
                    start = end6,
                    end = start1,
                ).right(),
            )
        }
}
