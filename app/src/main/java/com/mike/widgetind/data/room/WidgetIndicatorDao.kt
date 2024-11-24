package com.mike.widgetind.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WidgetIndicatorDao {
    @Query("SELECT * FROM widget_info WHERE widgetId = :widgetId")
    suspend fun getWidgetInfo(widgetId: Int): WidgetIndicatorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(widgetIndicatorEntity: WidgetIndicatorEntity)

    @Query("DELETE FROM widget_info WHERE widgetId = :widgetId")
    suspend fun removeWidgetInfo(widgetId: Int)

    @Query("DELETE FROM widget_info")
    suspend fun clear()

    @Query("SELECT * FROM widget_info")
    suspend fun getAll(): List<WidgetIndicatorEntity>
}