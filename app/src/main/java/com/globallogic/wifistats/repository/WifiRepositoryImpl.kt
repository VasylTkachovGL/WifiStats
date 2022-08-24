package com.globallogic.wifistats.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.globallogic.wifistats.model.WifiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class WifiRepositoryImpl @Inject constructor(private val context: Context) : WifiRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun collectConnectedWifiData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            subscribeToNetworkEvents()
        } else {
            val info = connectivityManager.activeNetworkInfo
            val isConnected = info?.isConnected == true
            if (isConnected) {
                val wifiInfo = wifiManager.connectionInfo
                wifiInfo?.let { getWifiInfo(wifiInfo) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun getScannedWifiInfo(): Flow<List<WifiData>> {
        return flow {
            val wifiDataList = mutableListOf<WifiData>()
            val scanResults = wifiManager.scanResults
            for (scanResult in scanResults) {
                wifiDataList.add(
                    WifiData(
                        scanResult.SSID,
                        scanResult.BSSID,
                        scanResult.frequency,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            scanResult.channelWidth
                        } else 0,
                        scanResult.level
                    )
                )
                Log.d("WIFI", "Scan result: $scanResult")
            }
            emit(wifiDataList)
        }.flowOn(Dispatchers.IO)
    }

    private fun getWifiInfo(wifiInfo: WifiInfo) {
        Log.d("WIFI", "ssid: ${wifiInfo.ssid}")
        Log.d("WIFI", "bssid: ${wifiInfo.bssid}")
        Log.d("WIFI", "MAC: ${wifiInfo.macAddress}")
        Log.d("WIFI", "Frequency: ${wifiInfo.frequency}")
        Log.d("WIFI", "RSSI: ${wifiInfo.rssi}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("WIFI", "rxLinkSpeedMbps: ${wifiInfo.rxLinkSpeedMbps}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("WIFI", "maxSignalLevel: ${wifiManager.maxSignalLevel}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("WIFI", "rxLinkSpeedMbps: ${wifiInfo.currentSecurityType}")
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