package com.practicum.playlistmaker.app

import android.app.Application
import com.practicum.playlistmaker.main.di.appModule
import com.practicum.playlistmaker.main.di.dataModule
import com.practicum.playlistmaker.main.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule, dataModule, domainModule)
        }
    }

    companion object {
        const val SHARED_PREFS = "SHARED_PREFS"
    }
}