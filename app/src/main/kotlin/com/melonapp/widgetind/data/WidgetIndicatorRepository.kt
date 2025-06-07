package com.melonapp.widgetind.data

import android.content.Context
import com.melonapp.widgetind.data.room.WidgetIndicatorDao
import com.melonapp.widgetind.data.room.WidgetIndicatorEntity
import com.melonapp.widgetind.ui.home.UiState

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

    suspend fun getAllEmptyEntitiesByTimeStampDesc(): UiState {
        return UiState(
            dao.getAll()
                .filter {
                    it.lastUpdate > 0L
                }.sortedByDescending {
                    it.lastUpdate
                }
        )
    }
}