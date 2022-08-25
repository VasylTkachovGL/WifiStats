package com.globallogic.wifistats.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globallogic.wifistats.model.ChannelWidth
import com.globallogic.wifistats.model.WifiData
import com.globallogic.wifistats.ui.common.BodyText

@Composable
fun WifiListItem(data: WifiData) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            BodyText("SSID: ${data.ssid}")
            BodyText("BSSID: ${data.bssid}")
            BodyText("Frequency: ${data.frequency}MHz")
            BodyText("Signal strength: ${data.rssi}")
            BodyText("Channel: ${data.channelWidth}")
            BodyText("Security types: ${data.capabilities}")
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
fun ListItemPreview() {
    WifiListItem(
        WifiData(
            "WifiSpot1",
            "d0:15:a6:a4:c8:63",
            2400,
            ChannelWidth.CHANNEL_WIDTH_20MHZ.title,
            -61,
            "[WPA2-PSK-CCMP]"
        )
    )
}