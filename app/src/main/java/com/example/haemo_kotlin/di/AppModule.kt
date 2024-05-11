package com.example.haemo_kotlin.di

import android.app.Application
import android.content.Context
import com.example.haemo_kotlin.service.MyFirebaseMessagingService
import com.example.haemo_kotlin.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): RetrofitClient {
        return RetrofitClient
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMyFirebaseMessagingService(): MyFirebaseMessagingService {
        return MyFirebaseMessagingService()
    }
}