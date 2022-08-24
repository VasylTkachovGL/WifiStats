package com.globallogic.wifistats.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globallogic.wifistats.model.WifiData

@Composable
fun WifiListItem(data: WifiData) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "SSID: ${data.ssid}",
            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
        Text(
            text = "BSSID: ${data.bssid}",
            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
        Text(
            text = "Frequency: ${data.frequency}",
            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
        Text(
            text = "Signal strength: ${data.rssi}",
            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
        Text(
            text = "Channel: ${data.channelWidth}",
            Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview
@Composable
fun ListItemPreview() {
    WifiListItem(WifiData("WifiSpot1", "d0:15:a6:a4:c8:63", 2400, 0, -61))
}