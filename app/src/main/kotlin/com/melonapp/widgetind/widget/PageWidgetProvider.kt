package com.melonapp.widgetind.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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

    // This is currently not supported because we can't dynamically set color of bg shape: circle_bg.xml
    // One alternative I can think of is enumerate a list of possible options backgrounds
    // e.g. circle_bg_blue, circle_bg_green, circle_bg_red and so on and match them with the closest colors from dynamic colors.
    private fun stashedDynamicColorSolution(context: Context, views: RemoteViews) {
        val attrsBg =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimaryInverse))
        val bgColor = attrsBg.getColor(0, Color.WHITE)
        attrsBg.recycle()
        val attrs =
            context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimaryInverse))
        val accentColor = attrs.getColor(0, Color.WHITE)
        attrs.recycle()

        views.setTextColor(R.id.pageNumber, accentColor)
        val tintedBitmap = getTintedIconBitmap(context, R.drawable.ic_home, accentColor)
        views.setImageViewBitmap(R.id.pageIcon, tintedBitmap)
    }

    private fun getTintedIconBitmap(context: Context, drawableRes: Int, tintColor: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableRes)!!.mutate()
        val wrapped = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrapped, tintColor)

        val width = wrapped.intrinsicWidth
        val height = wrapped.intrinsicHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        wrapped.setBounds(0, 0, width, height)
        wrapped.draw(canvas)

        return bitmap
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