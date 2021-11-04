package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.jacob.wakatimeapp.R.drawable
import com.jacob.wakatimeapp.common.models.UserDetails
import com.jacob.wakatimeapp.common.ui.theme.Typography
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme

@Composable
fun UserDetailsSection(userDetails: UserDetails?) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = rememberImagePainter(
                data = userDetails?.photoUrl,
                builder = {
                    transformations(CircleCropTransformation())
                    placeholder(drawable.place_holder)
                    fallback(drawable.place_holder)

                }
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
        )
    )
}
