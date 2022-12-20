package com.jacob.wakatimeapp.search.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.cardHeader
import com.jacob.wakatimeapp.core.ui.theme.cardSubtitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails

@Composable
fun ProjectsList(projects: List<ProjectDetails>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        items(projects) { project ->
            ProjectListItem(project)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListItem(project: ProjectDetails) {
    val cardShape = RoundedCornerShape(percent = 25)
    val spacing = MaterialTheme.spacing
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = cardShape,
        shadowElevation = 10.dp,
        tonalElevation = 2.dp,
        onClick = {},
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
                    text = "2 Hours, 2 Mins",
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
