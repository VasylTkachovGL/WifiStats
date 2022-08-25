package com.globallogic.wifistats.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import androidx.annotation.RequiresApi
import com.globallogic.wifistats.model.ChannelWidth
import com.globallogic.wifistats.model.ConnectedWifiData
import com.globallogic.wifistats.model.WifiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class WifiRepositoryImpl @Inject constructor(context: Context) : WifiRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val _connectedWifiFlow: MutableStateFlow<ConnectedWifiData?> = MutableStateFlow(null)
    private val connectedWifiFlow: StateFlow<ConnectedWifiData?> = _connectedWifiFlow

    override fun collectConnectedWifiData(): StateFlow<ConnectedWifiData?> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            subscribeToNetworkEvents()
        } else {
            if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                val wifiInfo = wifiManager.connectionInfo
                wifiInfo?.let { getWifiInfo(wifiInfo) }
            }
        }
        return connectedWifiFlow
    }

    @SuppressLint("MissingPermission")
    override fun getScannedWifiInfo(): Flow<List<WifiData>> {
        return flow {
            val wifiDataList = mutableListOf<WifiData>()
            val scanResults = wifiManager.scanResults
            for (scanResult in scanResults) {
                val channelWidth =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ChannelWidth.fromValue(scanResult.channelWidth).title
                    } else {
                        ChannelWidth.CHANNEL_WIDTH_20MHZ.title
                    }
                wifiDataList.add(
                    WifiData(
                        scanResult.SSID,
                        scanResult.BSSID,
                        scanResult.frequency,
                        channelWidth,
                        scanResult.level,
                        scanResult.capabilities
                    )
                )
                Log.d("WIFI", "Scan result: $scanResult")
            }
            emit(wifiDataList)
        }.flowOn(Dispatchers.IO)
    }

    private fun getWifiInfo(wifiInfo: WifiInfo) {
        _connectedWifiFlow.value = ConnectedWifiData(
            wifiInfo.ssid,
            wifiInfo.bssid,
            Formatter.formatIpAddress(wifiInfo.ipAddress),
            wifiInfo.rssi
        )
        Log.d("WIFI", "Ssid: ${wifiInfo.ssid}")
        Log.d("WIFI", "Bssid: ${wifiInfo.bssid}")
        Log.d("WIFI", "Ip address: ${wifiInfo.ipAddress}")
        Log.d("WIFI", "Frequency: ${wifiInfo.frequency}")
        Log.d("WIFI", "RSSI: ${wifiInfo.rssi}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("WIFI", "rxLinkSpeedMbps: ${wifiInfo.rxLinkSpeedMbps}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("WIFI", "maxSignalLevel: ${wifiManager.maxSignalLevel}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun subscribeToNetworkEvents() {
        val request: NetworkRequest =
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
        val networkCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(
                    network: Network,
                    capabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, capabilities)
                    val isConnected =
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    Log.d("WIFI", "Has capability: $isConnected")
                    val wifiInfo = capabilities.transportInfo as WifiInfo?
                    wifiInfo?.let { getWifiInfo(it) }
                }
            }
        connectivityManager.requestNetwork(request, networkCallback)
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}