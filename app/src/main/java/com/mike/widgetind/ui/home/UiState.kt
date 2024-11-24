package com.mike.widgetind.ui.home

import com.mike.widgetind.data.room.WidgetIndicatorEntity

data class UiState(
    val list: List<WidgetIndicatorEntity> = emptyList()
)