package com.melonapp.widgetind.di

import androidx.room.Room
import com.melonapp.widgetind.data.WidgetIndicatorRepository
import com.melonapp.widgetind.data.room.MIGRATION_1_2
import com.melonapp.widgetind.data.room.WidgetIndicatorDatabase
import com.melonapp.widgetind.ui.about.AboutViewModel
import com.melonapp.widgetind.ui.home.HomeViewModel
import com.melonapp.widgetind.ui.widgetsettings.WidgetSettingsViewModel
import com.melonapp.widgetind.widget.PageWidgetProviderUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { WidgetIndicatorRepository(get(), get()) }

    single {
        Room.databaseBuilder(
            get(),
            WidgetIndicatorDatabase::class.java,
            "widget_ind_db"
        ).addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<WidgetIndicatorDatabase>().widgetIndDao() }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { WidgetSettingsViewModel(get()) }
    viewModel { AboutViewModel(get()) }

    factory { PageWidgetProviderUseCase(get()) }
}