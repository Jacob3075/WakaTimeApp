package com.jacob.wakatimeapp.home.domain.models

data class Project(
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Double,
)
