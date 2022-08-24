package com.globallogic.wifistats.repository

import android.annotation.SuppressLint
import com.globallogic.wifistats.model.WifiData
import kotlinx.coroutines.flow.Flow

interface WifiRepository {
    fun collectConnectedWifiData()
    fun getScannedWifiInfo(): Flow<List<WifiData>>
}