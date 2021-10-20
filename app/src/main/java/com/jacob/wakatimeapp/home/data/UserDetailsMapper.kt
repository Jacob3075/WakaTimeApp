package com.jacob.wakatimeapp.home.data

import com.jacob.wakatimeapp.common.data.DtoMapper
import com.jacob.wakatimeapp.common.models.UserDetails
import com.jacob.wakatimeapp.home.data.dtos.GetUserDetailsResDTO

class UserDetailsMapper : DtoMapper<GetUserDetailsResDTO, UserDetails> {
    override fun fromDtoToModel(dto: GetUserDetailsResDTO) = UserDetails(
        bio = dto.data.bio,
        email = dto.data.email,
        id = dto.data.id,
        timeout = dto.data.timeout,
        timezone = dto.data.timezone,
        username = dto.data.username,
        displayName = dto.data.displayName,
        lastProject = dto.data.lastProject,
        fullName = dto.data.fullName,
        durationsSliceBy = dto.data.durationsSliceBy,
        createdAt = dto.data.createdAt,
        dateFormat = dto.data.dateFormat,
        photoUrl = dto.data.photoUrl,
    )

    override fun fromModelToDto(model: UserDetails): GetUserDetailsResDTO {
        TODO("Not yet implemented")
    }
}