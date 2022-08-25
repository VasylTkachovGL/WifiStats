package com.globallogic.wifistats.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.*
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
import java.net.Inet6Address
import java.net.InetAddress
import javax.inject.Inject


class WifiRepositoryImpl @Inject constructor(context: Context) : WifiRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private var activeNetwork: Network? = null

    private val _connectedWifiFlow: MutableStateFlow<ConnectedWifiData?> = MutableStateFlow(null)
    private val connectedWifiFlow: StateFlow<ConnectedWifiData?> = _connectedWifiFlow

    override fun collectConnectedWifiData(): StateFlow<ConnectedWifiData?> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            subscribeToNetworkEvents()
        } else {
            if (connectivityManager.activeNetworkInfo?.isConnected == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activeNetwork = connectivityManager.activeNetwork
                }
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
        val routes: List<RouteInfo>? = connectivityManager.getLinkProperties(activeNetwork)?.routes
        var gateway: InetAddress? = null
        if (routes != null) {
            for (route in routes) {
                if (route.isDefaultRoute && route.gateway !is Inet6Address) {
                    gateway = route.gateway
                }
            }
        }
        val ipAddress = Formatter.formatIpAddress(wifiInfo.ipAddress)
        val dnsServers = connectivityManager.getLinkProperties(activeNetwork)?.dnsServers

        _connectedWifiFlow.value = ConnectedWifiData(
            wifiInfo.ssid,
            wifiInfo.bssid,
            ipAddress,
            gateway,
            dnsServers,
            wifiInfo.rssi
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("WIFI", "rxLinkSpeedMbps: ${wifiInfo.rxLinkSpeedMbps}")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("WIFI", "maxSignalLevel: ${wifiManager.maxSignalLevel}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun subscribeToNetworkEvents() {
        val request: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        val networkCallback: ConnectivityManager.NetworkCallback =
            object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(
                    network: Network,
                    capabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, capabilities)
                    val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    if (isConnected) {
                        activeNetwork = network
                        val wifiInfo = capabilities.transportInfo as WifiInfo?
                        wifiInfo?.let { getWifiInfo(it) }
                    }
                }
            }
        connectivityManager.requestNetwork(request, networkCallback)
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}