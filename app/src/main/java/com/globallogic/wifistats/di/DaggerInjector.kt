package com.globallogic.wifistats.di

import android.content.Context

class DaggerInjector {
    companion object {
        private lateinit var instance: AppComponent

        fun buildComponent(context: Context): AppComponent {
            instance = DaggerAppComponent.factory().create(context)
            return instance
        }
    }

}