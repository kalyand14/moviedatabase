package com.android.omdb

import android.app.Application
import com.android.omdb.core.logging.LineNumberDebugTree
import com.android.omdb.features.movie.di.appModule
import com.android.omdb.features.movie.di.repoModule
import com.android.omdb.features.movie.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

open class App : Application() {
    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(LineNumberDebugTree())

        startKoin()
    }

    open val baseUrl: String = BuildConfig.BASE_URL

    protected open fun startKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }

    }
}