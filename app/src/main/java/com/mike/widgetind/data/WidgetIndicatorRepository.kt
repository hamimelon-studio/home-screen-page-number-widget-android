package com.mike.widgetind.data

import android.content.Context
import com.mike.widgetind.data.room.WidgetIndicatorDao
import com.mike.widgetind.data.room.WidgetIndicatorEntity
import com.mike.widgetind.ui.home.UiState
import kotlin.math.roundToInt

class WidgetIndicatorRepository(
    private val appContext: Context,
    private val dao: WidgetIndicatorDao
) {
    suspend fun fetchApp(input: WidgetIndicatorEntity): WidgetIndicatorEntity? {
        return null
    }

    suspend fun getSteamObEntity(widgetId: Int): WidgetIndicatorEntity? {
        return dao.getObApps(widgetId)
    }

    suspend fun getSteamObEntities(): UiState {
        return UiState(
            dao.getAllObApps()
        )
    }

    suspend fun update(entity: WidgetIndicatorEntity) {
        dao.save(entity)
    }

    suspend fun delete(widgetId: Int) {
        dao.removeObApp(widgetId)
    }

    private fun adjustDiscountValue(discountGiven: Int, initialPrice: Long, finalPrice: Long): Int {
        if (discountGiven > 0) {
            return discountGiven
        }
        val adjusted = (initialPrice - finalPrice) * 1.0f / initialPrice * 100.0f
        return adjusted.roundToInt()
    }
}