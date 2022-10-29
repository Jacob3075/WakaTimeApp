package com.jacob.wakatimeapp.home.domain

import arrow.core.right
import com.jacob.wakatimeapp.core.models.Stats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.home.domain.RecalculateLatestStreakServiceRobot.Companion.createDailyStats
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RecalculateLatestStreakServiceTest {
    private val robot = RecalculateLatestStreakServiceRobot()

    @Test
    internal fun `when called with duration and there is no valid streak, then return empty streak`() =
        runTest {
            val start = "2022-03-01"
            val end = "2022-02-01"

            robot.buildService()
                .mockGetDataForRange(
                    start,
                    end,
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(30, emptyList(), end.toLocalDate()),
                        range = StatsRange(start.toLocalDate(), end.toLocalDate())
                    ).right()
                )
                .calculate(
                    start = start.toLocalDate(),
                    value = 1,
                    unit = DateTimeUnit.MONTH,
                )
                .resultShouldBe(StreakRange.ZERO.right())
                .verifyGetDataForRange(start, end)
        }

    @Test
    internal fun `when there are non-continuous stats that include end of the duration, then return latest streak`() =
        runTest {
            val days = listOf(0, 1, 2, 5, 6, 7, 8, 11, 12, 13, 14)
            val start = LocalDate(2022, 3, 31)
            val end = LocalDate(2022, 3, 17)

            robot.buildService()
                .mockGetDataForRange(
                    start.toString(),
                    end.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(
                            size = 15,
                            days = days,
                            end = end
                        ),
                        range = StatsRange(start, end)
                    ).right()
                )
                .calculate(
                    start = start,
                    value = 2,
                    unit = DateTimeUnit.WEEK,
                )
                .resultShouldBe(
                    StreakRange(
                        start = LocalDate(2022, 3, 28),
                        end = start
                    ).right()
                )
                .verifyGetDataForRange(start.toString(), end.toString())
        }

    @Test
    internal fun `when there are none continuous stats that do not include end of the duration, then return empty streak`() =
        runTest {
            val days = listOf(0, 1, 2, 5, 6, 7, 8, 10, 11, 12)
            val start = "2022-03-31"
            val end = "2022-03-17"

            robot.buildService()
                .mockGetDataForRange(
                    start,
                    end,
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(14, days, end.toLocalDate()),
                        range = StatsRange(start.toLocalDate(), end.toLocalDate())
                    ).right()
                )
                .calculate(
                    start = start.toLocalDate(),
                    value = 2,
                    unit = DateTimeUnit.WEEK,
                )
                .resultShouldBe(StreakRange.ZERO.right())
                .verifyGetDataForRange(start, end)
        }

    @Test
    internal fun `when entire duration is part of the streak, then should itself again with next duration`() =
        runTest {
            val start = "2022-03-01"
            val end = "2022-02-01"
            val count = end.toLocalDate().daysUntil(start.toLocalDate()) + 1
            val days = List(count) { it }
            val secondStart = end.toLocalDate().minus(1, DateTimeUnit.DAY).toString()
            val secondEnd = secondStart.toLocalDate().minus(1, DateTimeUnit.MONTH).toString()

            robot.buildService()
                .mockGetDataForRange(
                    start,
                    end,
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end.toLocalDate()),
                        range = StatsRange(start.toLocalDate(), end.toLocalDate())
                    ).right(),

                )
                .mockGetDataForRange(
                    secondStart,
                    secondEnd,
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, emptyList(), end.toLocalDate()),
                        range = StatsRange(
                            start.toLocalDate().minus(1, DateTimeUnit.MONTH),
                            end.toLocalDate().minus(1, DateTimeUnit.MONTH)
                        )
                    ).right()
                )
                .calculate(
                    start = start.toLocalDate(),
                    value = 1,
                    unit = DateTimeUnit.MONTH,
                )
                .verifyGetDataForRange(start, end)
                .verifyGetDataForRange(
                    secondStart,
                    secondEnd
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

            robot.buildService()
                .mockGetDataForRange(
                    start.toString(),
                    end.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end),
                        range = StatsRange(start, end)
                    ).right(),
                )
                .mockGetDataForRange(
                    secondStart.toString(),
                    secondEnd.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days2, secondEnd),
                        range = StatsRange(
                            start.minus(1, DateTimeUnit.MONTH),
                            end.minus(1, DateTimeUnit.MONTH)
                        )
                    ).right()
                )
                .calculate(
                    start = start,
                    value = 2,
                    unit = DateTimeUnit.WEEK,
                )
                .resultShouldBe(
                    StreakRange(
                        start = "2022-03-13".toLocalDate(),
                        end = "2022-03-31".toLocalDate()
                    ).right()
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

            robot.buildService()
                .mockGetDataForRange(
                    start.toString(),
                    end.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end),
                        range = StatsRange(start, end)
                    ).right(),
                )
                .mockGetDataForRange(
                    secondStart.toString(),
                    secondEnd.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days2, secondEnd),
                        range = StatsRange(
                            start.minus(1, DateTimeUnit.MONTH),
                            end.minus(1, DateTimeUnit.MONTH)
                        )
                    ).right()
                )
                .calculate(
                    start = start,
                    value = 2,
                    unit = DateTimeUnit.WEEK,
                )
                .resultShouldBe(
                    StreakRange(
                        start = "2022-03-17".toLocalDate(),
                        end = "2022-03-31".toLocalDate()
                    ).right()
                )
        }

    @Test
    internal fun `when streak lasts for multiple months, then result should be sum of all the streaks`() =
        runTest {
            val start1 = LocalDate(2022, 4, 30)
            val end1 = start1.minus(1, DateTimeUnit.MONTH)
            val start2 = end1.minus(1, DateTimeUnit.DAY)
            val end2 = start2.minus(1, DateTimeUnit.MONTH)
            val start3 = end2.minus(1, DateTimeUnit.DAY)
            val end3 = start3.minus(1, DateTimeUnit.MONTH)
            val start4 = end3.minus(1, DateTimeUnit.DAY)
            val end4 = start4.minus(1, DateTimeUnit.MONTH)
            val start5 = end4.minus(1, DateTimeUnit.DAY)
            val end5 = start5.minus(1, DateTimeUnit.MONTH)
            val count = end1.daysUntil(start1) + 1
            val days = List(count) { it }

            robot.buildService()
                .mockGetDataForRange(
                    start1.toString(),
                    end1.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end1),
                        range = StatsRange(start1, end1)
                    ).right(),
                )
                .mockGetDataForRange(
                    start2.toString(),
                    end2.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end2),
                        range = StatsRange(start2, end2)
                    ).right(),
                )
                .mockGetDataForRange(
                    start3.toString(),
                    end3.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(
                            count,
                            days.take(end3.daysUntil(start3) + 1),
                            end3
                        ),
                        range = StatsRange(start3, end3)
                    ).right(),
                )
                .mockGetDataForRange(
                    start4.toString(),
                    end4.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, days, end4),
                        range = StatsRange(start4, end4)
                    ).right(),
                )
                .mockGetDataForRange(
                    start5.toString(),
                    end5.toString(),
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(count, emptyList(), end5),
                        range = StatsRange(start5, end5)
                    ).right(),
                )
                .calculate(
                    start = start1,
                    value = 1,
                    unit = DateTimeUnit.MONTH,
                )
                .resultShouldBe(
                    StreakRange(
                        start = end4,
                        end = start1,
                    ).right()
                )
        }
}
