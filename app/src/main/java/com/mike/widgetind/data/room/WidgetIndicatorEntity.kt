package com.mike.widgetind.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mike.widgetind.R

@Entity(tableName = "widget_info")
data class WidgetIndicatorEntity(
    @PrimaryKey
    val widgetId: Int,
    val pageNumber: Int = 1,
    val iconRes: Int = R.drawable.ic_home
)