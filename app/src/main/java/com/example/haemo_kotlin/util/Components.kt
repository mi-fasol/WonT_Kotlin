package com.example.haemo_kotlin.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haemo_kotlin.R

@Composable
fun ErrorScreen(text: String) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = R.drawable.error_image),
                contentDescription = null,
                modifier = Modifier.size((screenWidth / 4).dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = text,
                color = Color(0xffc2c2c2),
                fontSize = 12.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
