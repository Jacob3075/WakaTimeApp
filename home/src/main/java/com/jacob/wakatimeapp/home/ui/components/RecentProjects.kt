package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.models.Project
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.cardHeader
import com.jacob.wakatimeapp.core.ui.theme.cardSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun RecentProjects(
    projectsWorkedOn: ImmutableList<Project>,
    onSeeAllClicked: () -> Unit,
    onProjectClicked: (String) -> Unit,
) = Column(
    modifier = Modifier.fillMaxWidth(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typography = MaterialTheme.typography
        Text(text = "Recent Projects", style = typography.sectionTitle)
        Text(
            text = "See All",
            color = MaterialTheme.colorScheme.primary,
            style = typography.sectionSubtitle,
            modifier = Modifier.clickable(onClick = onSeeAllClicked),
        )
    }
    RecentProjectList(
        projects = projectsWorkedOn,
        onProjectClicked = onProjectClicked,
    )
}

@Composable
private fun RecentProjectList(
    projects: ImmutableList<Project>,
    onProjectClicked: (String) -> Unit,
) = Column(
    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sMedium),
) {
    projects.take(n = 3).map { ProjectCardItem(it, onClick = onProjectClicked) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectCardItem(project: Project, onClick: (String) -> Unit) {
    val cardShape = RoundedCornerShape(percent = 25)
    val spacing = MaterialTheme.spacing
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = cardShape,
        shadowElevation = 10.dp,
        tonalElevation = 2.dp,
        onClick = { onClick(project.name) },
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(spacing.medium),
        ) {
            Column(
                Modifier
                    .weight(1f, fill = true),
            ) {
                Text(text = project.name, style = MaterialTheme.typography.cardHeader)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${project.time.hours} Hours, ${project.time.minutes} Mins",
                    style = MaterialTheme.typography.cardSubtitle,
                )
            }
            Image(
                painter = painterResource(id = MaterialTheme.assets.icons.arrow),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
                contentDescription = "",
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun RecentProjectPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        RecentProjects(
            projectsWorkedOn = listOf(
                Project(Time(10, 9, 0f), "Project 1", 75.0),
                Project(Time(100, 26, 0f), "Project 2", 20.0),
                Project(Time(5, 15, 0f), "Project 3", 10.0),
            ).toImmutableList(),
            onSeeAllClicked = {},
            onProjectClicked = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProjectCardItemPreview() = WakaTimeAppTheme {
    Surface {
        ProjectCardItem(
            project = Project(
                Time.ZERO,
                "Project 1",
                0.0,
            ),
            onClick = {},
        )
    }
}
