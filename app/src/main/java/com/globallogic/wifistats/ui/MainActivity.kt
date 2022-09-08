package com.globallogic.wifistats.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.globallogic.wifistats.ui.theme.WifiStatsTheme
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

class MainActivity : ComponentActivity() {

    lateinit var viewModel: BaseViewModel
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getWifiStats()
            }
            return@registerForActivityResult
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)

        setContent {
            WifiStatsTheme {
                CollectorScreen(
                    deviceID = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    ),
                    wifiItemsData = viewModel.wifiData,
                    connectedWifiData = viewModel.connectedWifiData
                ) {
                    getWifiStats()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_LOCATION && resultCode == Activity.RESULT_OK) {
            getWifiStats()
        }
    }

    private fun getWifiStats() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                viewModel.collectWifiData()
            } else {
                showEnableLocationDialog()
            }
        }
    }

    private fun checkPermission(): Boolean =
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            false
        } else {
            true
        }


    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lm.isLocationEnabled
        } else {
            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }
    }

    private fun showEnableLocationDialog() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(2000)
            .setFastestInterval(1000)

        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(settingsBuilder.build())
        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (apiException: ApiException) {
                when (apiException.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvableException = apiException as ResolvableApiException
                            startIntentSenderForResult(
                                resolvableException.resolution.intentSender,
                                REQUEST_ENABLE_LOCATION,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(TAG, "PendingIntent unable to execute request")
                        } catch (e: ClassCastException) {
                            Log.e(TAG, "Location api exception is not resolvable")
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        Log.e(TAG, "Something wrong with locations service")
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "WIFI"
        private const val REQUEST_ENABLE_LOCATION = 101
    }
}