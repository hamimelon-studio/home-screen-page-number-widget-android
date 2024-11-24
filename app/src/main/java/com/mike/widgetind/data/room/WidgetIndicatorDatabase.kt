package com.mike.widgetind.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WidgetIndicatorEntity::class
    ], version = 1
)
abstract class WidgetIndicatorDatabase : RoomDatabase() {

    abstract fun widgetIndDao(): WidgetIndicatorDao
}