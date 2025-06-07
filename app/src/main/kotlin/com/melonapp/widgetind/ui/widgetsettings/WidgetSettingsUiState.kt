package com.melonapp.widgetind.ui.widgetsettings

import com.melonapp.widgetind.R

sealed class WidgetSettingsUiState {
    data object Idle : WidgetSettingsUiState()

    data class InputDialog(
        val widgetId: Int,
        val pageNumber: Int = 1,
        val iconRes: Int = R.drawable.ic_home
    ) : WidgetSettingsUiState()

    data class Debug(
        val widgetId: Int,
        val pageNumber: Int,
        val iconRes: Int,
        val delay: Long
    ) : WidgetSettingsUiState()
}