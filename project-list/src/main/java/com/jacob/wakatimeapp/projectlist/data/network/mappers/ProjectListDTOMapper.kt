package com.jacob.wakatimeapp.projectlist.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.projectlist.data.network.dto.ProjectDetailsDTO
import com.jacob.wakatimeapp.projectlist.data.network.dto.ProjectListDTO

fun ProjectListDTO.toModel() = data.map(ProjectDetailsDTO::toModel)
