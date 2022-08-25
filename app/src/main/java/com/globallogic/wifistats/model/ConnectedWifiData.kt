package com.globallogic.wifistats.model

import java.net.InetAddress

data class ConnectedWifiData(
    val ssid: String,
    val bssid: String,
    val ipAddress: String,
    val gateway: InetAddress?,
    val dnsServers: MutableList<InetAddress>?,
    val rssi: Int,
)
