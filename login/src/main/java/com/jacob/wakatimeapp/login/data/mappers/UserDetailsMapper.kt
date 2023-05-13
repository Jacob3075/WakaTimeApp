package com.jacob.wakatimeapp.login.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.login.data.dtos.GetUserDetailsResDTO
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun GetUserDetailsResDTO.toModel() = UserDetails(
    bio = data.bio,
    email = data.email,
    id = data.id,
    timeout = data.timeout,
    timezone = TimeZone.of(data.timezone),
    username = data.username,
    displayName = data.displayName,
    lastProject = data.lastProject,
    fullName = data.fullName,
    durationsSliceBy = data.durationsSliceBy,
    createdAt = Instant.parse(data.createdAt).toLocalDateTime(TimeZone.of(data.timezone)).date,
    dateFormat = data.dateFormat,
    photoUrl = "${data.photoUrl}?s=420",
)
