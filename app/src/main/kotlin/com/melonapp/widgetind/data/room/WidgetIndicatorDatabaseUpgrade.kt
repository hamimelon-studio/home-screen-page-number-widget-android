package com.melonapp.widgetind.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the new column with a default value
        database.execSQL("ALTER TABLE widget_info ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0")
    }
}