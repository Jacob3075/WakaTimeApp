package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.R
import com.jacob.wakatimeapp.common.ui.theme.Colors
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme

@Composable
fun RecentProjects() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Recent Projects", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "See All", color = Colors.AccentText, fontSize = 14.sp)
        }

        RecentProjectList(modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
private fun RecentProjectList(modifier: Modifier = Modifier) {
    LazyColumn {
        items(count = 3) {
            ProjectCardItem(project = "")
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ProjectCardItem(project: String) {
    Box(
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(25))
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 2.dp)
            .background(color = Colors.CardBGPrimary, shape = RoundedCornerShape(25))
            .clickable {  }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column(
                Modifier
                    .weight(1f, fill = true)
            ) {
                Text(text = "Arm Simulator", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "103 Hours, 47 Mins", fontSize = 14.sp, fontWeight = FontWeight.Light)
            }
            Image(painter = painterResource(id = R.drawable.ic_arrow), contentDescription = "")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RecentProjectPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        RecentProjects()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProjectCardItemPreview() = WakaTimeAppTheme {
    Surface {
        ProjectCardItem(project = "")
    }
}
