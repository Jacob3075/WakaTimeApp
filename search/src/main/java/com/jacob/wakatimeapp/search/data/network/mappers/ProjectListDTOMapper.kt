package com.jacob.wakatimeapp.search.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.search.data.network.dto.ProjectDetailsDTO
import com.jacob.wakatimeapp.search.data.network.dto.ProjectListDTO

fun ProjectListDTO.toModel() = data.map(ProjectDetailsDTO::toModel)
