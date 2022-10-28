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
    internal fun `when there are none continuous stats that include end of the duration, then return latest streak`() =
        runTest {
            val days = listOf(0, 1, 2, 5, 6, 7, 8, 11, 12, 13)
            val start = "2022-03-31"
            val end = "2022-03-17"

            robot.buildService()
                .mockGetDataForRange(
                    start,
                    end,
                    Stats(
                        totalTime = Time.ZERO,
                        dailyStats = createDailyStats(
                            size = 14,
                            days = days,
                            end = end.toLocalDate()
                        ),
                        range = StatsRange(start.toLocalDate(), end.toLocalDate())
                    ).right()
                )
                .calculate(
                    start = start.toLocalDate(),
                    value = 2,
                    unit = DateTimeUnit.WEEK,
                )
                .resultShouldBe(
                    StreakRange(
                        start = start.toLocalDate().minus(2, DateTimeUnit.DAY),
                        start.toLocalDate()
                    ).right()
                )
                .verifyGetDataForRange(start, end)
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
}
