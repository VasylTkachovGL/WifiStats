package com.globallogic.wifistats.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import javax.inject.Inject

class WifiRepositoryImpl @Inject constructor(private val context: Context) : WifiRepository {

    override fun collectWifiData() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= 29) {
            val activeNetwork = connectivityManager.activeNetwork ?: return
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return
            val isActiveNetworkValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            Log.d("WIFI", "Network capability validated: $isActiveNetworkValidated")
            Log.d("WIFI", "Signal strength: ${capabilities.signalStrength}")
            Log.d("WIFI", "Transport info: ${capabilities.transportInfo.toString()}")
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= 29) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            val isActiveNetworkValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            Log.d("WIFI", "Network capability validated: $isActiveNetworkValidated")
            return isActiveNetworkValidated
        } else {
            val info = connectivityManager.activeNetworkInfo
            val isConnected = info?.isConnected == true
            Log.d("WIFI", "Network connected: $isConnected")
            return isConnected
        }
    }
}