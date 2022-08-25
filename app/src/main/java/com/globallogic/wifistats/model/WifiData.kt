package com.globallogic.wifistats.model

import android.net.NetworkCapabilities

data class WifiData(
    val ssid: String,
    val bssid: String,
    val frequency: Int,
    val channelWidth: String,
    val rssi: Int,
    val capabilities: String
)
