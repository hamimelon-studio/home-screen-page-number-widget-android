package com.mike.widgetind.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mike.widgetind.data.WidgetIndicatorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val appContext: Context,
    private val repository: WidgetIndicatorRepository
) : ViewModel() {
    val isRefresh = MutableStateFlow(false)

    val uiState = MutableStateFlow<UiState?>(null)

    init {
        forceRefresh()
    }

    fun forceRefresh() {
        viewModelScope.launch {
            try {
                isRefresh.value = true
                uiState.value = repository.getSteamObEntities()
            } catch (e: Exception) {
                Log.d("bbbb", "Exception: $e")
                e.printStackTrace()
                uiState.value = null
            } finally {
                delay(100)
                withContext(Dispatchers.Main) {
                    isRefresh.value = false
                }
            }
        }
    }
}