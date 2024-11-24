package com.mike.widgetind.ui.widgetsettings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mike.widgetind.R
import com.mike.widgetind.data.WidgetIndicatorRepository
import com.mike.widgetind.data.room.WidgetIndicatorEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WidgetSettingsViewModel(
    private val appContext: Context,
    private val repository: WidgetIndicatorRepository
) : ViewModel() {
    val pageNumber = MutableStateFlow(1)
    val iconRes = MutableStateFlow(R.drawable.ic_home)

    fun save(widgetId: Int, pageNumber: Int, iconRes: Int) {
        viewModelScope.launch {
            repository.update(WidgetIndicatorEntity(
                widgetId = widgetId,
                pageNumber = pageNumber,
                iconRes = iconRes
            ))
        }
    }

    fun load(widgetId: Int) {
        viewModelScope.launch {
            val widgetInfo = repository.getWidgetIndEntity(widgetId)
            widgetInfo?.let {
                pageNumber.value = widgetInfo.pageNumber
                iconRes.value = widgetInfo.iconRes
            }
        }
    }
}