package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.cardContent
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
internal fun StatsCard(
    statsType: String,
    statsValue: String,
    gradient: Gradient,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    roundedCornerPercent: Int = 25,
    statsTypeWeight: Float = 1f,
    statsValueWeight: Float = 0.5f,
    iconOffset: Dp = 50.dp,
    iconSize: Dp = 80.dp,
) {
    val cardGradient = Brush.horizontalGradient(
        listOf(
            gradient.startColor,
            gradient.endColor,
        ),
    )
    val cardShape = RoundedCornerShape(roundedCornerPercent)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable { onClick() }
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = MaterialTheme.spacing.lMedium),
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = iconOffset)
                .size(iconSize),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = statsType,
                maxLines = 2,
                modifier = Modifier.weight(statsTypeWeight, true),
                style = MaterialTheme.typography.cardContent,
                color = gradient.onStartColor,
            )
            Text(
                text = statsValue,
                modifier = Modifier.weight(statsValueWeight, true),
                textAlign = TextAlign.End,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                ),
                color = gradient.onEndColor,
            )
        }
    }
}

@WtaPreviews
@Composable
private fun StatsCardPreview() = WakaTimeAppTheme {
    Surface {
        Row {
            StatsCard(
                statsType = "Stats Type",
                statsValue = "Stats Value",
                gradient = MaterialTheme.gradients.reef,
                iconId = MaterialTheme.assets.icons.time,
                onClick = {},
            )
        }
    }
}
