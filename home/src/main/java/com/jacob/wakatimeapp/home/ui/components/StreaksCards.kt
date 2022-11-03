package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import kotlinx.datetime.LocalDate

@Composable
internal fun CurrentStreakCard(currentStreak: StreakRange, modifier: Modifier = Modifier) {
    val gradientColor = MaterialTheme.gradients.greenCyan
    val gradientBrush = Brush.horizontalGradient(gradientColor.colorList)
    val shape = RoundedCornerShape(20)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradientBrush, shape)
    ) {
        Image(
            painter = painterResource(id = MaterialTheme.assets.icons.time),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gradientColor.onEndColor),
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
                color = gradientColor.onStartColor,
                modifier = Modifier.removeFontPadding(streakValueTextStyle)
            )
            Text(
                text = "Current Streak",
                color = gradientColor.onStartColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
            )
        }
    }
}

/**
 * Removes excess padding from the top and bottom of the text.
 *
 * [Source](https://issuetracker.google.com/issues/171394808#comment38)
 */
fun Modifier.removeFontPadding(
    textStyle: TextStyle,
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val heightWithoutPadding =
        placeable.height - placeable.height.mod(textStyle.lineHeight.roundToPx())
    layout(placeable.width, heightWithoutPadding) {
        placeable.placeRelative(0, 0)
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
                modifier = Modifier.weight(0.5F)
            )
            CurrentStreakCard(
                currentStreak = StreakRange(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 5)
                ),
                modifier = Modifier.weight(0.5F)
            )
        }
    }
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Full Device",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
annotation class WtaPreviews
