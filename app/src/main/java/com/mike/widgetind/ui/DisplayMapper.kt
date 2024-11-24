package com.mike.widgetind.ui

import android.content.Context
import com.mike.widgetind.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DisplayMapper {
    fun toPriceDisplay(context: Context, rrp: Long, finalPrice: Long): String {
        return if (rrp == finalPrice) {
            formatAud(context, finalPrice)
        } else {
            "${formatAud(context, finalPrice)} (${formatAud(context, rrp)})"
        }
    }

    private fun formatAud(content: Context, price: Long): String =
        content.getString(R.string.currency_formater, price / 100.0f)

    fun toDiscountDisplay(context: Context, discount: Int): String {
        return if (discount == 0) {
            context.getString(R.string.no_discount)
        } else {
            context.getString(R.string.discount_off, discount)
        }
    }

    fun toWidgetDisplayTime(context: Context, ms: Long): String {
        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - ms
        val timeFormat =
            SimpleDateFormat(context.getString(R.string.time_formater), Locale.getDefault())
        val dateTime = Date(ms)

        // Define thresholds for relative times
        val oneDay = TimeUnit.DAYS.toMillis(1)
        val twoDays = oneDay * 2
        val oneWeek = oneDay * 7
        val oneMonth = oneDay * 30
        val oneYear = oneDay * 365

        return when {
            elapsed < oneDay -> context.getString(
                R.string.last_update_today,
                timeFormat.format(dateTime)
            )

            elapsed < twoDays -> context.getString(R.string.last_update_yday)
            elapsed < oneWeek -> context.getString(R.string.last_update_days_ago, elapsed / oneDay)
            elapsed < oneMonth -> context.getString(
                R.string.last_update_weeks_ago,
                elapsed / oneWeek
            )
            elapsed < oneYear -> context.getString(R.string.last_update_mos_ago, elapsed / oneMonth)
            else -> context.getString(R.string.last_update_yrs_ago, elapsed / oneYear)
        }
    }
}