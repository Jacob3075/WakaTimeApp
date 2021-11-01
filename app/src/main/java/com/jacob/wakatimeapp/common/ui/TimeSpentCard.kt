package com.jacob.wakatimeapp.common.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.ui.theme.Gradient
import com.jacob.wakatimeapp.common.ui.theme.Gradients
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme

@Composable
fun TimeSpentCard(
    gradient: Gradient,
    roundedCornerPercent: Int,
    @DrawableRes iconId: Int,
    mainText: String,
    time: Time,
) {
    val cardGradient =
        Brush.horizontalGradient(listOf(gradient.startColor, gradient.endColor))
    val cardShape = RoundedCornerShape(roundedCornerPercent)
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = 22.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = 95.dp)
                .size(80.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mainText,
                maxLines = 2,
                modifier = Modifier.weight(1f, true),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                ),
            )
            Text(
                text = "${time.hours}H, ${time.minutes}M",
                modifier = Modifier.weight(1f, true),
                textAlign = TextAlign.End,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                )
            )
        }
    }
}

@Composable
fun TimeSpentChip() {
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TimeSpentCardPreview() = WakaTimeAppTheme(darkTheme = true) {
    TimeSpentCard(
        Gradients.primary,
        25,
        R.drawable.ic_time,
        "Total Time Spent Today",
        Time(42, 22)
    )
}
