package com.jacob.wakatimeapp.core.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle

/**
 * Removes excess padding from the top and bottom of the text.
 *
 * [Source](https://issuetracker.google.com/issues/171394808#comment38)
 */
fun Modifier.removeFontPadding(
    textStyle: TextStyle,
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val heightWithoutPadding =
        placeable.height - placeable.height.mod(textStyle.lineHeight.roundToPx())
    layout(placeable.width, heightWithoutPadding) {
        placeable.placeRelative(0, 0)
    }
}
