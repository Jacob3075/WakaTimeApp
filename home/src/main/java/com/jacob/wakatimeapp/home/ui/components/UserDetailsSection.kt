package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest.Builder
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.pageTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun UserDetailsSection(
    userDetails: UserDetails?,
    modifier: Modifier = Modifier,
) = Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.fillMaxWidth()
) {
    Image(
        painter = rememberAsyncImagePainter(
            Builder(LocalContext.current).data(data = userDetails?.photoUrl)
                .apply {
                    val icons = MaterialTheme.assets.icons
                    transformations(CircleCropTransformation())
                    placeholder(icons.placeholder)
                    fallback(icons.placeholder)
                }
                .build()
        ),
        contentDescription = "Profile image",
        modifier = Modifier.size(58.dp)
    )
    Text(
        text = userDetails?.fullName.orEmpty(),
        style = MaterialTheme.typography.pageTitle.copy(
            baselineShift = BaselineShift(multiplier = 0.3f),
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserDetailsPreview() = WakaTimeAppTheme {
    UserDetailsSection(
        UserDetails(
            bio = "",
            email = "",
            id = "",
            timeout = 0,
            timezone = "",
            username = "",
            displayName = "",
            lastProject = "",
            fullName = "Jacob Bosco",
            durationsSliceBy = "",
            createdAt = "",
            dateFormat = "",
            photoUrl = ""
        )
    )
}
