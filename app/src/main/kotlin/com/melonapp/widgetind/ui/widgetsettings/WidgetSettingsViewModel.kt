package com.melonapp.widgetind.ui.widgetsettings

import android.appwidget.AppWidgetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonapp.widgetind.R
import com.melonapp.widgetind.data.WidgetIndicatorRepository
import com.melonapp.widgetind.data.room.WidgetIndicatorEntity
import com.melonapp.widgetind.ui.widgetsettings.CustomisedIntentConstant.ACTION_IN_APP_ADD_WIDGET_ENTRY
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WidgetSettingsViewModel(
    private val repository: WidgetIndicatorRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<WidgetSettingsUiState>(WidgetSettingsUiState.Idle)
    val uiState: StateFlow<WidgetSettingsUiState> = _uiState

    fun save(widgetId: Int, pageNumber: Int, iconRes: Int) {
        viewModelScope.launch {
            repository.update(
                WidgetIndicatorEntity(
                    widgetId = widgetId,
                    pageNumber = pageNumber,
                    iconRes = iconRes
                )
            )
        }
    }

    fun load(widgetId: Int, intentAction: String?, onFinish: () -> Unit) {
        viewModelScope.launch(IO) {
            val isFromHomeScreenEntry = intentAction == AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
            val widgetInfo = repository.getWidgetIndEntity(widgetId)
            _uiState.value =
                when {
                    isFromHomeScreenEntry -> WidgetSettingsUiState.InputDialog(widgetId)

                    intentAction == ACTION_IN_APP_ADD_WIDGET_ENTRY -> {
                        val unconfiguredEntities =
                            repository.getAllEmptyEntitiesByTimeStampDesc()
                        val lastOne = unconfiguredEntities.list.firstOrNull()
                        Log.d("Lifecycle", "add from inapp, lastOne: $lastOne")
                        lastOne?.let {
                            val delay = System.currentTimeMillis() - lastOne.lastUpdate
                            Log.d("Lifecycle", "add from inapp, delay: $delay")
                            if (delay > 2000) {
                                onFinish.invoke()
                                WidgetSettingsUiState.Idle
                            } else {
                                WidgetSettingsUiState.InputDialog(
                                    widgetId = lastOne.widgetId,
                                    pageNumber = widgetInfo?.pageNumber ?: 1,
                                    iconRes = widgetInfo?.iconRes ?: R.drawable.ic_home
                                )
                            }
                        } ?: WidgetSettingsUiState.Idle
                    }

                    else -> WidgetSettingsUiState.InputDialog(
                        widgetId = widgetId,
                        pageNumber = widgetInfo?.pageNumber ?: 1,
                        iconRes = widgetInfo?.iconRes ?: R.drawable.ic_home
                    )
                }
        }
    }
}