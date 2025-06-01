package com.melonapp.widgetind

import android.app.Application
import com.melonapp.widgetind.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class WidgetIndicatorApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WidgetIndicatorApplication)
            modules(appModule)
        }
    }
}