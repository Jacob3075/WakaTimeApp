package com.jacob.data.mappers

import com.jacob.data.dtos.GetUserDetailsResDTO
import com.jacob.data.models.UserDetails

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