package com.mike.widgetind.di

import androidx.room.Room
import com.mike.widgetind.data.WidgetIndicatorRepository
import com.mike.widgetind.data.room.WidgetIndicatorDatabase
import com.mike.widgetind.ui.about.AboutViewModel
import com.mike.widgetind.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { WidgetIndicatorRepository(get(), get()) }

    single {
        Room.databaseBuilder(
            get(),
            WidgetIndicatorDatabase::class.java,
            "steam_ob_db"
        ).build()
    }

    single { get<WidgetIndicatorDatabase>().steamObDao() }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { AboutViewModel(get()) }
}