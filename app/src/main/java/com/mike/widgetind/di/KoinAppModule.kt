package com.mike.widgetind.di

import androidx.room.Room
import com.mike.widgetind.data.WidgetIndicatorRepository
import com.mike.widgetind.data.room.WidgetIndicatorDatabase
import com.mike.widgetind.ui.about.AboutViewModel
import com.mike.widgetind.ui.home.HomeViewModel
import com.mike.widgetind.ui.widgetsettings.WidgetSettingsViewModel
import com.mike.widgetind.widget.ClockWidgetProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { WidgetIndicatorRepository(get(), get()) }

    single {
        Room.databaseBuilder(
            get(),
            WidgetIndicatorDatabase::class.java,
            "widget_ind_db"
        ).build()
    }

    single { get<WidgetIndicatorDatabase>().widgetIndDao() }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { WidgetSettingsViewModel(get(), get()) }
    viewModel { AboutViewModel(get()) }
}