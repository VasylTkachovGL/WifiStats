package com.globallogic.wifistats

import android.os.Bundle
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
                CollectorScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}