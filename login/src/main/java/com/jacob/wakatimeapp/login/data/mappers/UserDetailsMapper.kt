package com.jacob.wakatimeapp.login.data.mappers

import com.jacob.wakatimeapp.core.data.mappers.DtoMapper
import com.jacob.wakatimeapp.core.models.UserDetails
import com.jacob.wakatimeapp.login.data.dtos.GetUserDetailsResDTO
import javax.inject.Inject

class UserDetailsMapper @Inject constructor() :
    DtoMapper<GetUserDetailsResDTO, UserDetails> {
    override fun fromDtoToModel(dto: GetUserDetailsResDTO) =
        com.jacob.wakatimeapp.core.models.UserDetails(
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
            photoUrl = "${dto.data.photoUrl}?s=420"
        )

    override fun fromModelToDto(model: com.jacob.wakatimeapp.core.models.UserDetails): GetUserDetailsResDTO {
        TODO("Not yet implemented")
    }
}
