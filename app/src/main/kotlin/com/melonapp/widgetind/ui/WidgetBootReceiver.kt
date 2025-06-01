package com.melonapp.widgetind.ui

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.melonapp.widgetind.widget.PageWidgetProvider

class WidgetBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Update all widgets when the device boots
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, PageWidgetProvider::class.java))
            for (appWidgetId in appWidgetIds) {
                // You can call your update method here to refresh the widget data
                PageWidgetProvider().onUpdate(context, appWidgetManager, intArrayOf(appWidgetId))
            }
        }
    }
}