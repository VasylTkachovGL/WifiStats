package com.globallogic.wifistats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.globallogic.wifistats.di.DaggerInjector
import com.globallogic.wifistats.model.WifiData
import com.globallogic.wifistats.repository.WifiRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: WifiRepository

    private val _wifiData = MutableLiveData<List<WifiData>>()
    val wifiData: LiveData<List<WifiData>> = _wifiData

    init {
        DaggerInjector.buildComponent(application.applicationContext).inject(this)
    }

    fun collectWifiData() {
        repository.collectConnectedWifiData()
        viewModelScope.launch {
            repository.getScannedWifiInfo().collect { wifiDataList ->
                _wifiData.value = wifiDataList
            }
        }
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