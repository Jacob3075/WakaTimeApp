package com.jacob.wakatimeapp.search.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ProjectsList(
    projects: ImmutableList<ProjectDetails>,
    onProjectItemClicked: (ProjectDetails) -> Unit,
    modifier: Modifier = Modifier,
) =
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.spacing.sMedium,
        ),
    ) {
        items(projects, key = { it.name }) { project ->
            ProjectListItem(
                project = project,
                modifier = Modifier.animateItemPlacement(),
                onClick = onProjectItemClicked,
            )
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListItem(
    project: ProjectDetails,
    modifier: Modifier = Modifier,
    onClick: (ProjectDetails) -> Unit,
) {
    val spacing = MaterialTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(percent = 25),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
        onClick = { onClick(project) },
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(spacing.medium),
        ) {
            Text(text = project.name, style = MaterialTheme.typography.cardHeader)
            Image(
                painter = painterResource(id = MaterialTheme.assets.icons.arrow),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
                contentDescription = null,
            )
        }
    }
}
