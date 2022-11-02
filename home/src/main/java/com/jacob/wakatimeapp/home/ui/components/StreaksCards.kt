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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients

@Composable
internal fun CurrentStreakCard(modifier: Modifier = Modifier) {
    val gradient =
        Brush.horizontalGradient(MaterialTheme.gradients.greenCyan.colorList)

    val shape = RoundedCornerShape(20f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradient, shape)
    ) {
        Image(
            painter = painterResource(id = MaterialTheme.assets.icons.time),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Column {
            Text(text = "5 days")
            Text(text = "Current Streak")
        }
    }
}

@Composable
internal fun LongestStreakCard(modifier: Modifier = Modifier) {
}

@WtaPreviews
@Composable
private fun CurrentStreakCardPreview() = WakaTimeAppTheme() {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CurrentStreakCard(modifier = Modifier.weight(0.5F))
            CurrentStreakCard(modifier = Modifier.weight(0.5F))
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
annotation class WtaPreviews
