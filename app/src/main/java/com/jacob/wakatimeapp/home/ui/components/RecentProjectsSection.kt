package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.ui.theme.Colors
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.home.domain.models.DailyStats
import com.jacob.wakatimeapp.home.domain.models.Project

@Composable
fun RecentProjects(dailyStats: DailyStats?) {
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
        RecentProjectList(
            modifier = Modifier.padding(horizontal = 12.dp),
            projects = dailyStats?.projectsWorkedOn ?: emptyList()
        )
    }
}

@Composable
private fun RecentProjectList(modifier: Modifier = Modifier, projects: List<Project>) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = projects) { ProjectCardItem(it) }
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ProjectCardItem(project: Project) {
    val cardShape = RoundedCornerShape(25)
    Box(
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = cardShape)
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 2.dp)
            .background(color = Colors.CardBGPrimary, shape = cardShape)
            .clickable { }
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
                Text(text = project.name, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${project.time.hours} Hours, ${project.time.minutes} Mins",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Image(painter = painterResource(id = R.drawable.ic_arrow), contentDescription = "")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RecentProjectPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        RecentProjects(
            DailyStats(
                timeSpent = Time(0, 0),
                projectsWorkedOn = listOf(
                    Project(Time(10, 9), "Project 1", 75.0),
                    Project(Time(100, 26), "Project 2", 20.0),
                    Project(Time(5, 15), "Project 3", 10.0),
                ),
                mostUsedLanguage = "",
                mostUsedEditor = "",
                mostUsedOs = ""
            )
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProjectCardItemPreview() = WakaTimeAppTheme {
    Surface {
        ProjectCardItem(Project(Time(0, 0), "Project 1", 0.0))
    }
}
