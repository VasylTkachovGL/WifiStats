package com.globallogic.wifistats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.globallogic.wifistats.di.DaggerInjector
import javax.inject.Inject

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: WifiRepository

    init {
        DaggerInjector.buildComponent(application.applicationContext).inject(this)
    }

    fun collectWifiData() {
        repository.collectWifiData()
    }

//    class BaseViewModelFactory(private val application: Application) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
//                return BaseViewModel(application) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
}