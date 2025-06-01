package com.melonapp.widgetind.ui.home

import com.melonapp.widgetind.data.room.WidgetIndicatorEntity

data class UiState(
    val list: List<WidgetIndicatorEntity> = emptyList()
)