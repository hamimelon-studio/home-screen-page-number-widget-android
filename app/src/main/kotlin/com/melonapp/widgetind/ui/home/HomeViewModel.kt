package com.melonapp.widgetind.ui.home

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonapp.widgetind.data.WidgetIndicatorRepository
import com.melonapp.widgetind.ui.widgetsettings.CustomisedIntentConstant.ACTION_IN_APP_ADD_WIDGET_ENTRY
import com.melonapp.widgetind.ui.widgetsettings.CustomisedIntentConstant.ACTION_IN_APP_CLICK_ENTRY
import com.melonapp.widgetind.ui.widgetsettings.WidgetSettingsActivity
import com.melonapp.widgetind.widget.PageWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val appContext: Context,
    private val repository: WidgetIndicatorRepository
) : ViewModel() {
    val isRefresh = MutableStateFlow(false)

    val uiState = MutableStateFlow<UiState?>(null)

    init {
        cleanUpInactiveWidgets {
            forceRefresh()
        }
    }

    private fun cleanUpInactiveWidgets(onComplete: () -> Unit) {
        viewModelScope.launch(IO) {
            val uiState = repository.getWidgetIndEntities()
            val activeWidgetList = getActiveWidgetList()
            Log.d(
                "HomeScreen",
                "allWidget in db: ${uiState.list.map { it.widgetId }.joinToString { "," }}"
            )
            Log.d("HomeScreen", "activeWidgetList: ${activeWidgetList.joinToString { "," }}")
            uiState.list.forEach {
                if (!activeWidgetList.contains(it.widgetId)) {
                    repository.delete(it.widgetId)
                    Log.d("HomeScreen", "cleanup widget ${it.widgetId} which isn't active.")
                }
            }
            delay(100)
            onComplete.invoke()
        }
    }

    fun forceRefresh() {
        viewModelScope.launch(IO) {
            try {
                isRefresh.value = true
                val list = repository.getWidgetIndEntities()
                Log.d("HomeScreen", "list.size: ${list.list.size}")
                list.list.forEach {
                    Log.d("HomeScreen", " game tile: $it")
                }
                uiState.value = list
            } catch (e: Exception) {
                Log.d("bbbb", "Exception: $e")
                e.printStackTrace()
                uiState.value = null
            } finally {
                delay(100)
                withContext(Dispatchers.Main) {
                    isRefresh.value = false
                }
            }
        }
    }

    fun launchConfigWidgetInputActivity(appWidgetId: Int) {
        val intent = getSetupConfigIntent(appContext, appWidgetId)
        appContext.startActivity(intent)
    }

    fun requestPinWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetProvider = ComponentName(context, PageWidgetProvider::class.java)
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            val successCallback = PendingIntent.getActivity(
                context,
                0,
                Intent(context, WidgetSettingsActivity::class.java).apply {
                    action = ACTION_IN_APP_ADD_WIDGET_ENTRY
                    Log.d("Lifecycle", "requestPinWidget")
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            appWidgetManager.requestPinAppWidget(widgetProvider, null, successCallback)
        }
    }

    private fun getSetupConfigIntent(context: Context, appWidgetId: Int): Intent {
        val intent = Intent(context, WidgetSettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            action = ACTION_IN_APP_CLICK_ENTRY
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        return intent
    }

    private fun getActiveWidgetList(): List<Int> {
        val appWidgetManager = AppWidgetManager.getInstance(appContext)
        val provider = ComponentName(appContext, PageWidgetProvider::class.java)

        val currentWidgetIds = appWidgetManager.getAppWidgetIds(provider)
        return currentWidgetIds.toList()
    }
}