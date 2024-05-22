package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.system.theme.ColorOption
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.viewModel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationSettingScreen(viewModel: MainViewModel, navController: NavController) {
    val mainColor by viewModel.colorState.collectAsState()
    val config = LocalConfiguration.current
    val checked by viewModel.notificationState.collectAsState()

    Scaffold(
        topBar = {
            SettingScreenAppBar("알림 설정", mainColor, navController = navController)
        },
        modifier = Modifier.background(colorResource(id = R.color.settingScreenBackgroundColor))
    ) {
        Column() {
            SettingTitleField(text = "푸시 알림 설정")
            SettingSwitch(
                "알림 설정",
                isChecked = checked,
                onCheckedChange = {
                    viewModel.changeNotificationAccess()
                },
                colorResId = R.color.mainColor
            )
        }
    }
}