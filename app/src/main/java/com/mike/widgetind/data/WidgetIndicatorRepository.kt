package com.mike.widgetind.data

import android.content.Context
import com.mike.widgetind.data.room.WidgetIndicatorDao
import com.mike.widgetind.data.room.WidgetIndicatorEntity
import com.mike.widgetind.ui.home.UiState

class WidgetIndicatorRepository(
    private val appContext: Context,
    private val dao: WidgetIndicatorDao
) {
    suspend fun getWidgetIndEntity(widgetId: Int): WidgetIndicatorEntity? {
        return dao.getWidgetInfo(widgetId)
    }

    suspend fun getWidgetIndEntities(): UiState {
        return UiState(
            dao.getAll()
        )
    }

    suspend fun update(entity: WidgetIndicatorEntity) {
        dao.save(entity)
    }

    suspend fun delete(widgetId: Int) {
        dao.removeWidgetInfo(widgetId)
    }
}