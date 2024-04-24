package com.example.haemo_kotlin.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.haemo_kotlin.R

@Composable
fun YesOrNoDialog(
    content: String,
    onClickCancel: () -> Unit,
    onClickConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        val conf = LocalConfiguration.current
        val screenHeight = conf.screenHeightDp
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 4.5).dp)
                .padding(horizontal = 20.dp)
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Text(content, fontSize = 16.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { onClickCancel() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xfff4f4f4),
                            contentColor = Color(0xff969696)
                        )
                    ) {
                        Text("아니오")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { onClickConfirm() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.mainColor),
                            contentColor = Color.White
                        )
                    ) {
                        Text("예")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog(
    content: String,
    onClickConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        val conf = LocalConfiguration.current
        val screenHeight = conf.screenHeightDp
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 4.5).dp)
                .padding(horizontal = 20.dp)
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Text(content, fontSize = 16.sp)
                }
                Button(
                    onClick = { onClickConfirm() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.mainColor),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Text("확인")
                }
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    Surface(
        Modifier.fillMaxSize()
    ) {
        YesOrNoDialog(content = "확인됏슘동", onClickCancel = {}) {

        }
    }
}