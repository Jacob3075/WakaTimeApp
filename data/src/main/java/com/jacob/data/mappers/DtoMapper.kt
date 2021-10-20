package com.jacob.data.mappers

interface DtoMapper<DTO, Model> {
    fun fromDtoToModel(dto: DTO): Model

    fun fromModelToDto(model: Model): DTO

    fun fromDtosToModels(dtos: List<DTO>): List<Model> = dtos.map(this::fromDtoToModel)
}