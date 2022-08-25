package com.globallogic.wifistats.model

data class ConnectedWifiData(
    val ssid: String,
    val bssid: String,
    val ipAddress: String,
    val rssi: Int,
)
