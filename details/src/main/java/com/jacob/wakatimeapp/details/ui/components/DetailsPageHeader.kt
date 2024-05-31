package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
internal fun DetailsPageHeader(projectName: String, modifier: Modifier = Modifier) = Text(
    modifier = modifier
        .padding(horizontal = MaterialTheme.spacing.small)
        .fillMaxWidth(),
    text = projectName,
    style = MaterialTheme.typography.displaySmall.copy(
        baselineShift = BaselineShift(multiplier = -0.1f),
    ),
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
)

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
