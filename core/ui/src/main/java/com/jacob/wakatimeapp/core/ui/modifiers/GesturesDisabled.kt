package com.jacob.wakatimeapp.core.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput

/**
 * [Source](https://stackoverflow.com/a/69146178/13181948)
 */
fun Modifier.gesturesDisabled(disabled: Boolean = true): Modifier {
    if (!disabled) return this

    return pointerInput(Unit) {
        awaitPointerEventScope {
            // we should wait for all new pointer events
            while (true) {
                awaitPointerEvent(pass = PointerEventPass.Initial)
                    .changes
                    .forEach(PointerInputChange::consume)
            }
        }
    }
}
