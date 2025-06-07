package com.melonapp.widgetind.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [WidgetIndicatorEntity::class], version = 2)
abstract class WidgetIndicatorDatabase : RoomDatabase() {

    abstract fun widgetIndDao(): WidgetIndicatorDao
}