package com.devlogs.rssfeed.common.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.devlogs.rssfeed.rss.RssUrlFinder
import com.devlogs.rssfeed.rss_parser.RssParser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideSharedPreference(appContext: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreInstance (): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideOkHttpClient () : OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideRssUrlFinder (httpClient: OkHttpClient) : RssUrlFinder {
        return RssUrlFinder(httpClient)
    }

    @Provides
    @Singleton
    fun provideRssParser (httpClient: OkHttpClient) : RssParser {
        return RssParser(httpClient)
    }
}