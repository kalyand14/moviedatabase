package com.android.omdb

class TestApp : App() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    override val baseUrl: String
        get() = "http://127.0.0.1:8080"

    companion object {
        private var sInstance: TestApp? = null

        @Synchronized
        fun getInstance(): TestApp {
            return sInstance!!
        }
    }
}