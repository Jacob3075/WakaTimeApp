package com.jacob.wakatimeapp.models

data class UserDetails(
    val bio: String,
    val email: String,
    val id: String,
    val timeout: Int,
    val timezone: String,
    val username: String,
    val displayName: String,
    val lastProject: String,
    val fullName: String,
    val durationsSliceBy: String,
    val createdAt: String,
    val dateFormat: String,
    val photoUrl: String,
)
