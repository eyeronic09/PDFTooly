package com.eyeronic09.freepdf

import android.app.Application
import com.eyeronic09.freepdf.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FreePdfApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FreePdfApp)
            modules(appModule)
        }
    }
}
