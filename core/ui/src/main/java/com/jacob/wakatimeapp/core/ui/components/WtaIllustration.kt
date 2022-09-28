package com.jacob.wakatimeapp.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WtaIllustration(
    illustrations: List<Int>,
    text: String,
    illustrationTestTag: String,
    modifier: Modifier = Modifier,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.fillMaxSize()
) {
    Image(
        painter = painterResource(id = illustrations.random()),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier.testTag(illustrationTestTag)
    )
    Spacer(modifier = Modifier.height(25.dp))
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
}
