package com.android.omdb

import android.app.Application
import com.android.omdb.core.logging.LineNumberDebugTree
import com.android.omdb.features.movie.di.appModule
import com.android.omdb.features.movie.di.repoModule
import com.android.omdb.features.movie.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(LineNumberDebugTree())
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}