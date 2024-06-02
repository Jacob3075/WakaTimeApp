package com.jacob.wakatimeapp.core.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jacob.wakatimeapp.core.common.data.local.dao.ApplicationDao
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.local.utils.WtaTypeConverters

@Database(entities = [DayEntity::class, ProjectPerDay::class], version = 1)
@TypeConverters(WtaTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun applicationDao(): ApplicationDao
}
