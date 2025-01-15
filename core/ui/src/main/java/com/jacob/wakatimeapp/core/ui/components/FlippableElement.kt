package com.jacob.wakatimeapp.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * [Rotating on Axis in 3D](https://www.youtube.com/watch?v=WdQUDHOwlgE&t=148s)
 * [Resetting Animation for each click](https://stackoverflow.com/questions/78620347/repeat-animation-when-user-clicks-jetpack-compose-android)
 * [Showing the back of the card correctly](https://medium.com/bilue/card-flip-animation-with-jetpack-compose-f60aaaad4ac9)
 */
@Composable
fun <T> rememberFlippableState(
    frontContent: @Composable T.() -> Unit = {},
    backContent: @Composable T.() -> Unit = {},
): FlippableElementData<T> {
    val isFlipped = remember { mutableStateOf(false) }
    val rotationXAnimation = animateFloatAsState(targetValue = if (isFlipped.value) 180f else 0f)

    val contentToShow = remember(rotationXAnimation.value) {
        if (rotationXAnimation.value < 90f) frontContent else backContent
    }

    val innerRotation = remember(rotationXAnimation.value) {
        if (rotationXAnimation.value < 90f) 0f else 180f
    }

    return FlippableElementData(
        isFlipped = isFlipped,
        rotationAnimationValue = rotationXAnimation,
        contentToShow = contentToShow,
        innerRotation = innerRotation,
    )
}

class FlippableElementData<T>(
    val isFlipped: MutableState<Boolean>,
    val rotationAnimationValue: State<Float>,
    val contentToShow: @Composable T.() -> Unit,
    val innerRotation: Float,
)
