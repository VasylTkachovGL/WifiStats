package com.globallogic.wifistats.repository

import com.globallogic.wifistats.model.ConnectedWifiData
import com.globallogic.wifistats.model.WifiData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WifiRepository {
    fun collectConnectedWifiData(): StateFlow<ConnectedWifiData?>
    fun getScannedWifiInfo(): Flow<List<WifiData>>
}