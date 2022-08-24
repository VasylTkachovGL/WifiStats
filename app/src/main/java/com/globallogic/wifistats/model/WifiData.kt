package com.globallogic.wifistats.model

data class WifiData(val ssid: String, val bssid: String, val frequency: Int, val channelWidth: Int, val rssi: Int)
