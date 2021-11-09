package com.jacob.wakatimeapp.home.ui.components

data class ShowIllustrationParameters(
    val illustrations: List<Int>,
    val text: String,
    val illustrationTestTag: String,
    val textTestTag: String,
)

data class ShowAnimationParameters(
    val animations: List<Int>,
    val text: String,
    val animationTestTag: String,
    val textTestTag: String,
)
