package com.melonapp.widgetind.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.melonapp.widgetind.R
import com.melonapp.widgetind.data.WidgetIndicatorRepository
import com.melonapp.widgetind.ui.widgetsettings.WidgetSettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get


class PageWidgetProvider : AppWidgetProvider() {
    companion object {
        const val ACTION_WIDGET_CONFIGURE = "com.your.package.ACTION_WIDGET_CONFIGURE"
    }

    private val repository: WidgetIndicatorRepository = get(WidgetIndicatorRepository::class.java)
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.number_widget)

        // Create the configuration intent
        val configIntent = Intent(context, WidgetSettingsActivity::class.java).apply {
            action = ACTION_WIDGET_CONFIGURE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val configPendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId,
            configIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Register the configuration intent
        views.setOnClickPendingIntent(R.id.frame, configPendingIntent)

        coroutineScope.launch {
            val widgetInfo = repository.getWidgetIndEntity(appWidgetId)
            widgetInfo?.let {
                views.setTextViewText(R.id.pageNumber, widgetInfo.pageNumber.toString())
                views.setImageViewResource(R.id.pageIcon, widgetInfo.iconRes)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        coroutineScope.launch(IO) {
            appWidgetIds?.forEach {
                repository.delete(it)
            }
        }
        super.onDeleted(context, appWidgetIds)
    }
}