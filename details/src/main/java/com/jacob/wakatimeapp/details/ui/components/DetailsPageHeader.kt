package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
internal fun DetailsPageHeader(projectName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = MaterialTheme.assets.icons.arrow),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
            contentDescription = null,
            modifier = Modifier.rotate(degrees = 180f).size(30.dp),
        )
        Text(
            text = projectName,
            style = MaterialTheme.typography.displaySmall.copy(
                baselineShift = BaselineShift(multiplier = -0.1f),
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@WtaComponentPreviews
@Composable
private fun DetailsPageHeaderPreview(
    @PreviewParameter(DetailPageHeaderPreviewProvider::class) projectName: String,
) = WakaTimeAppTheme {
    Surface {
        DetailsPageHeader(projectName)
    }
}

private class DetailPageHeaderPreviewProvider : CollectionPreviewParameterProvider<String>(
    listOf(
        "Project Name",
        "Some very long project name",
    ),
)
