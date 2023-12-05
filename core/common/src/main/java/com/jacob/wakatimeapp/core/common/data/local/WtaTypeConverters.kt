package com.jacob.wakatimeapp.core.common.data.local

import androidx.room.TypeConverter
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Branch
import com.jacob.wakatimeapp.core.models.project.Machine
import com.jacob.wakatimeapp.core.models.secondarystats.Editor
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Language
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystem
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("TooManyFunctions")
class WtaTypeConverters {
    @TypeConverter
    fun toLocalDate(date: Int) = LocalDate.fromEpochDays(date)

    @TypeConverter
    fun fromLocalDate(date: LocalDate) = date.toEpochDays()

    @TypeConverter
    fun toTime(time: String) = Json.decodeFromString<Time>(time)

    @TypeConverter
    fun fromTime(time: Time) = Json.encodeToString(time)

    @TypeConverter
    fun toEditors(editors: String) = Editors(Json.decodeFromString<List<Editor>>(editors))

    @TypeConverter
    fun fromEditors(editors: Editors) = Json.encodeToString(editors.values)

    @TypeConverter
    fun toLanguages(languages: String) = Languages(Json.decodeFromString<List<Language>>(languages))

    @TypeConverter
    fun fromLanguages(languages: Languages) = Json.encodeToString(languages.values)

    @TypeConverter
    fun toOperatingSystems(operatingSystems: String) =
        OperatingSystems(Json.decodeFromString<List<OperatingSystem>>(operatingSystems))

    @TypeConverter
    fun fromOperatingSystems(operatingSystems: OperatingSystems) = Json.encodeToString(operatingSystems.values)

    @TypeConverter
    fun toMachines(machine: String) = Json.decodeFromString<List<Machine>>(machine)

    @TypeConverter
    fun fromMachines(machine: List<Machine>) = Json.encodeToString(machine)

    @TypeConverter
    fun toBranch(branch: String) = Json.decodeFromString<List<Branch>>(branch)

    @TypeConverter
    fun fromBranch(branch: List<Branch>) = Json.encodeToString(branch)
}
