package com.example.haemo_kotlin.screen.setting.setting

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.viewModel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotificationSettingScreen(viewModel: MainViewModel, navController: NavController) {
    val mainColor by viewModel.colorState.collectAsState()
    val context = LocalContext.current
    val checked by viewModel.notificationState.collectAsState()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            viewModel.changeNotificationAccess(isGranted)
        }
    )

    LaunchedEffect(Unit, key2 = checked) {
        Log.d("미란 알림 상황", checked.toString())
    }

    Scaffold(
        topBar = {
            SettingScreenAppBar("알림 설정", mainColor, navController = navController)
        },
        modifier = Modifier.background(colorResource(id = R.color.settingScreenBackgroundColor))
    ) {
        Column {
            SettingTitleField(text = "푸시 알림 설정")
            SettingSwitch(
                "알림 설정",
                isChecked = checked,
                onCheckedChange = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) !=
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.changeNotificationAccess(it)
                        }
                    }
                },
                colorResId = mainColor
            )
        }
    }
}