package com.jacob.wakatimeapp.core.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val bio: String,
    val email: String,
    val id: String,
    val timeout: Int,
    val timezone: TimeZone,
    val username: String,
    val displayName: String,
    val lastProject: String,
    val fullName: String,
    val durationsSliceBy: String,
    val createdAt: LocalDate,
    val dateFormat: String,
    val photoUrl: String,
)
