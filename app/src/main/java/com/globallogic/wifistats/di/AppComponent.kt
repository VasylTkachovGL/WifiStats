package com.globallogic.wifistats.di

import android.content.Context
import com.globallogic.wifistats.ui.BaseViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(viewModel: BaseViewModel)
}