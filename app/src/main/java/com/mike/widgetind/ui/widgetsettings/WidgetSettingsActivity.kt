package com.mike.widgetind.ui.widgetsettings

import AddWidgetDialog
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mike.widgetind.R
import com.mike.widgetind.data.WidgetIndicatorRepository
import com.mike.widgetind.ui.theme.WidgetIndTheme
import com.mike.widgetind.widget.ClockWidgetProvider
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.get

class WidgetSettingsActivity : ComponentActivity() {
    private val repository: WidgetIndicatorRepository = get(WidgetIndicatorRepository::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        enableEdgeToEdge()
        val entity = runBlocking {
            withContext(IO) {
                repository.getWidgetIndEntity(widgetId)
            }
        }
        setContent {
            WidgetIndTheme {
                var showDialog by remember { mutableStateOf(true) }
                val viewModel: WidgetSettingsViewModel = koinViewModel()

                if (showDialog) {
                    AddWidgetDialog(
                        pageNumber = entity?.pageNumber ?: 1,
                        iconRes = entity?.iconRes ?: R.drawable.ic_home,
                        showDialog = showDialog,
                        onDismiss = {
                            showDialog = false
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        onConfirm = { selectedPage, selectedIcon ->
                            // Handle confirm logic here
                            showDialog = false

                            viewModel.save(widgetId, selectedPage, selectedIcon)
                            updateWidget(widgetId)

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
        val intent = Intent(this, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        // Update the widget
        appWidgetManager.updateAppWidget(
            appWidgetId,
            RemoteViews(packageName, R.layout.number_widget)
        )

        // Alternatively, you could call your specific update method:
        ClockWidgetProvider().onUpdate(this, appWidgetManager, intArrayOf(appWidgetId))
    }
}