package com.globallogic.wifistats.ui

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.globallogic.wifistats.R
import com.globallogic.wifistats.model.ConnectedWifiData
import com.globallogic.wifistats.model.WifiData
import com.globallogic.wifistats.ui.common.BodyText
import com.globallogic.wifistats.ui.common.HeaderText

@Composable
fun CollectorScreen(
    deviceID: String,
    wifiItemsData: LiveData<List<WifiData>>,
    connectedWifiData: LiveData<ConnectedWifiData>,
    onCollectPressed: () -> Unit
) {
    val availableWifiState = wifiItemsData.observeAsState()
    val connectedWifiState = connectedWifiData.observeAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Button(
                onClick = { onCollectPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.collect_wifi_data).uppercase(),
                    style = TextStyle(
                        fontWeight = FontWeight.W700,
                        fontSize = 18.sp
                    )
                )
            }
            connectedWifiState.value?.let { data ->
                Spacer(modifier = Modifier.height(8.dp))
                HeaderText("Device info")
                Spacer(modifier = Modifier.height(4.dp))
                Card(shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        BodyText(text = "Manufacture: ${Build.MANUFACTURER}")
                        BodyText(text = "Model: ${Build.MODEL}")
                        BodyText(text = "DeviceID $deviceID")
                        BodyText(text = "Version: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HeaderText("Connected WiFi")
                Spacer(modifier = Modifier.height(4.dp))
                Card(shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        BodyText(text = "SSID: ${data.ssid}")
                        BodyText(text = "BSSID: ${data.bssid}")
                        BodyText(text = "IP address: ${data.ipAddress}")
                        if (data.gateway != null) {
                            BodyText(text = "Gateway: ${data.gateway}")
                        }
                        Row {
                            BodyText("DNS: ")
                            if (data.dnsServers != null)
                                for (dnsServer in data.dnsServers) {
                                    BodyText("${dnsServer.hostAddress} ")
                                }
                        }
                        BodyText("Signal strength: ${data.rssi}")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            availableWifiState.value?.let { list ->
                HeaderText(text = "Available WiFi")
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn {
                    items(list.size) { index ->
                        WifiListItem(list[index])
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CollectorScreenPreview() {
    CollectorScreen("0000", MutableLiveData(), MutableLiveData()) {}
}