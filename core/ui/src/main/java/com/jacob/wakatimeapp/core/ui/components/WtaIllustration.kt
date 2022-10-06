package com.jacob.wakatimeapp.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun WtaIllustration(
    @DrawableRes illustration: Int,
    text: String,
    illustrationTestTag: String,
    modifier: Modifier = Modifier,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.fillMaxSize()
) {
    Image(
        painter = painterResource(id = illustration),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier.testTag(illustrationTestTag)
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.lMedium))
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}
