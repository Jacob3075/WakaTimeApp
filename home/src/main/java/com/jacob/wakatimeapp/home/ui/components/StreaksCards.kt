package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.modifiers.removeFontPadding
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import kotlinx.datetime.LocalDate

@Composable
internal fun CurrentStreakCard(
    currentStreak: StreakRange,
    gradient: Gradient,
    cornerPercentage: Int,
    modifier: Modifier = Modifier,
) {
    val gradientBrush = Brush.horizontalGradient(gradient.colorList)
    val shape = RoundedCornerShape(cornerPercentage)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradientBrush, shape)
    ) {
        Image(
            painter = painterResource(id = MaterialTheme.assets.icons.time),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gradient.onEndColor),
            modifier = Modifier
                .padding(
                    end = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.extraSmall
                )
                .size(size = 50.dp)
                .align(Alignment.BottomEnd)
        )
        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small,
                ),
        ) {
            val streakValueTextStyle = MaterialTheme.typography.displayMedium

            Text(
                text = buildAnnotatedString {
                    withStyle(style = streakValueTextStyle.toSpanStyle()) {
                        append(
                            currentStreak.days.toString()
                        )
                    }
                    withStyle(style = MaterialTheme.typography.headlineSmall.toSpanStyle()) {
                        append(" days")
                    }
                },
                color = gradient.onStartColor,
                modifier = Modifier.removeFontPadding(streakValueTextStyle)
            )
            Text(
                text = "Current Streak",
                color = gradient.onStartColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
            )
        }
    }
}

@Composable
internal fun LongestStreakCard(modifier: Modifier = Modifier): Unit = TODO(modifier.toString())

@WtaPreviews
@Composable
private fun CurrentStreakCardPreview() = WakaTimeAppTheme {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CurrentStreakCard(
                currentStreak = StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 11)
                ),
                gradient = MaterialTheme.gradients.purpink,
                cornerPercentage = 20,
                modifier = Modifier.weight(0.5F)
            )
            CurrentStreakCard(
                currentStreak = StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 5)
                ),
                gradient = MaterialTheme.gradients.tealLove,
                cornerPercentage = 20,
                modifier = Modifier.weight(0.5F)
            )
        }
    }
}
