package com.devlogs.rssfeed.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
       return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}