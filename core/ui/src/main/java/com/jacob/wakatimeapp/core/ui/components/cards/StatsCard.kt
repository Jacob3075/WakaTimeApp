package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.ui.theme.Gradient

@Composable
internal fun StatsCard(
    gradient: Gradient,
    @DrawableRes iconId: Int,
    mainText: String,
    text: String,
    onClick: () -> Unit,
    roundedCornerPercent: Int = 25,
    weights: Pair<Float, Float> = Pair(1f, 1f),
    iconOffset: Int = 50,
    iconSize: Int = 80
) {
    val cardGradient =
        Brush.horizontalGradient(
            listOf(
                gradient.startColor,
                gradient.endColor
            )
        )
    val cardShape = RoundedCornerShape(roundedCornerPercent)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable { onClick() }
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = 22.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = iconOffset.dp)
                .size(iconSize.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mainText,
                maxLines = 2,
                modifier = Modifier.weight(weights.first, true),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = text,
                modifier = Modifier.weight(weights.second, true),
                textAlign = TextAlign.End,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
