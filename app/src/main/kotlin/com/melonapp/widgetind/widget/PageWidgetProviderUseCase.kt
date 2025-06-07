package com.melonapp.widgetind.widget

import com.melonapp.widgetind.data.WidgetIndicatorRepository
import com.melonapp.widgetind.data.room.WidgetIndicatorEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageWidgetProviderUseCase(private val repository: WidgetIndicatorRepository) {
    fun asyncFetchEntityByWidgetId(widgetId: Int, callback: (WidgetIndicatorEntity?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = repository.getWidgetIndEntity(widgetId)
            callback.invoke(entity)
        }
    }

    fun saveWidgetIfNewCreated(widgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val entity = repository.getWidgetIndEntity(widgetId)
            if (entity == null) {
                val newEntity = WidgetIndicatorEntity(
                    widgetId = widgetId,
                    lastUpdate = System.currentTimeMillis()
                )
                repository.update(newEntity)
            }
        }
    }

    fun deleteWidgets(widgetIdList: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            widgetIdList.forEach {
                repository.delete(it)
            }
        }
    }
}