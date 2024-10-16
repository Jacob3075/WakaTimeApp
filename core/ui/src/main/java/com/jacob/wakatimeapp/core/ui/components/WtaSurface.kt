package com.jacob.wakatimeapp.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WtaSurface(modifier: Modifier = Modifier, onClick: () -> Unit = {}, content: @Composable () -> Unit) {
    val cardShape = RoundedCornerShape(percent = 25)
    Surface(
        modifier = modifier,
        shape = cardShape,
        shadowElevation = 10.dp,
        tonalElevation = 2.dp,
        onClick = onClick,
    ) {
        content()
    }
}
