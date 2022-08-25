package com.globallogic.wifistats.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.globallogic.wifistats.di.DaggerInjector
import com.globallogic.wifistats.model.ConnectedWifiData
import com.globallogic.wifistats.model.WifiData
import com.globallogic.wifistats.repository.WifiRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BaseViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: WifiRepository

    private val _wifiData = MutableLiveData<List<WifiData>>()
    val wifiData: LiveData<List<WifiData>> = _wifiData

    private val _connectedWifiData = MutableLiveData<ConnectedWifiData>()
    val connectedWifiData: LiveData<ConnectedWifiData> = _connectedWifiData

    init {
        DaggerInjector.buildComponent(application.applicationContext).inject(this)
    }

    fun collectWifiData() {
        viewModelScope.launch {
            repository.getScannedWifiInfo().collect { wifiDataList ->
                _wifiData.value = wifiDataList
            }
            repository.collectConnectedWifiData().collect { data ->
                _connectedWifiData.value = data
            }
        }
    }
}