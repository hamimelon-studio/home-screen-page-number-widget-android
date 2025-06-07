package com.melonapp.widgetind.ui.widgetsettings

import AddWidgetDialog
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.melonapp.widgetind.R
import com.melonapp.widgetind.ui.theme.WidgetIndTheme
import com.melonapp.widgetind.widget.PageWidgetProvider
import org.koin.androidx.compose.koinViewModel

class WidgetSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        setContent {
            WidgetIndTheme {
                val viewModel: WidgetSettingsViewModel = koinViewModel()
                val uiState = viewModel.uiState.collectAsState(WidgetSettingsUiState.Idle)
                LaunchedEffect(widgetId, intent.action) {
                    viewModel.load(widgetId, intent.action) {
                        finish()
                    }
                }

                if (uiState.value is WidgetSettingsUiState.InputDialog) {
                    val uiStateCast = uiState.value as WidgetSettingsUiState.InputDialog
                    AddWidgetDialog(
                        widgetId = uiStateCast.widgetId,
                        pageNumber = uiStateCast.pageNumber,
                        iconRes = uiStateCast.iconRes,
                        showDialog = true,
                        onDismiss = {
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        onConfirm = { actualWidgetId, selectedPage, selectedIcon ->
                            viewModel.save(actualWidgetId, selectedPage, selectedIcon)
                            updateWidget(actualWidgetId)

                            val resultValue = Intent().apply {
                                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                            }
                            setResult(RESULT_OK, resultValue)
                            finish()
                        }
                    )
                }
            }
        }
    }

    private fun updateWidget(appWidgetId: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        // Create an Intent to update the widget
        val intent = Intent(this, PageWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        // Update the widget
        appWidgetManager.updateAppWidget(
            appWidgetId,
            RemoteViews(packageName, R.layout.number_widget)
        )

        // Alternatively, you could call your specific update method:
        PageWidgetProvider().onUpdate(this, appWidgetManager, intArrayOf(appWidgetId))
    }
}