package com.globallogic.wifistats

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.globallogic.wifistats.model.WifiData
import com.globallogic.wifistats.screen.WifiListItem

@Composable
fun CollectorScreen(
    wifiItems: LiveData<List<WifiData>>,
    onCollectPressed: () -> Unit
) {
    val items = wifiItems.observeAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    Log.d("WIFI", "click!")
                    onCollectPressed()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.collect_wifi_data),
                    style = TextStyle(
                        fontWeight = FontWeight.W900,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn() {
                items.value?.let {
                    items(it.size) { index ->
                        WifiListItem(it[index])
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CollectorScreenPreview() {
    CollectorScreen(MutableLiveData()) {}
}