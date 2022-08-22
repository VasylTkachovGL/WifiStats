package com.globallogic.wifistats

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.globallogic.wifistats.ui.theme.WifiStatsTheme

class MainActivity : ComponentActivity() {

    lateinit var viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)

        setContent {
            WifiStatsTheme {
                CollectorScreen {
                    viewModel.collectWifiData()
                }
            }
        }

        Log.d("WIFI", getSystemInfo())
    }

    @SuppressLint("HardwareIds")
    fun getSystemInfo(): String {
        return "Manufacture: ${Build.MANUFACTURER} \n" +
                "Model: ${Build.MODEL} \n" +
                "Version Code: ${Build.VERSION.RELEASE} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "ID: ${Build.ID}"
    }
}