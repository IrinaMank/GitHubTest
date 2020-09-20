package com.mankovskaya

import android.app.Application
import com.mankovskaya.githubtest.system.di.appModule
import com.mankovskaya.githubtest.system.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule, networkModule)
        }
    }
}