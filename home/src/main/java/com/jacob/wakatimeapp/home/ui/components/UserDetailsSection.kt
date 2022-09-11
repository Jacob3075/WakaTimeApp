package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest.Builder
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.core.ui.R
import com.jacob.wakatimeapp.core.ui.theme.Typography
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme

@Composable
fun UserDetailsSection(
    userDetails: UserDetails?,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                Builder(LocalContext.current).data(data = userDetails?.photoUrl)
                    .apply {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.place_holder)
                        fallback(R.drawable.place_holder)
                    }.build()
            ),
            contentDescription = "Profile image",
            modifier = Modifier.size(58.dp),
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = userDetails?.fullName ?: "",
            fontSize = Typography.h4.fontSize,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserDetailsPreview() = WakaTimeAppTheme {
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
        ),
    )
}
