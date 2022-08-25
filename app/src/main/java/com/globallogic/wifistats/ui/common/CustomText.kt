package com.globallogic.wifistats.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.globallogic.wifistats.ui.theme.TextBodyColor

@Composable
fun HeaderText(text: String) {
    Text(
        style = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 16.sp
        ),
        text = text.uppercase(),
        textAlign = TextAlign.Start,
        color = TextBodyColor
    )
}

@Composable
fun BodyText(text: String) {
    Text(
        style = TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        ),
        text = text,
        textAlign = TextAlign.Start,
        color = TextBodyColor
    )
}

@Preview
@Composable
fun TextPreview() {
    Column {
        HeaderText(text = "Header")
        Spacer(modifier = Modifier.height(4.dp))
        BodyText(text = "Body")
    }
}