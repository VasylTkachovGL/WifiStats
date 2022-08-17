package com.globallogic.wifistats.di

import android.content.Context
import com.globallogic.wifistats.WifiRepository
import com.globallogic.wifistats.WifiRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule(val context: Context) {

    @Binds
    abstract fun provideUserRepository(repository: WifiRepositoryImpl): WifiRepository
}