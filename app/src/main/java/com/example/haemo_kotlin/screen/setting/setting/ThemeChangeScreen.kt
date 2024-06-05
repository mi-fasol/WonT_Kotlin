package com.example.haemo_kotlin.screen.setting.setting

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
import com.example.haemo_kotlin.ui.theme.switchText
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.viewModel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ThemeChangeScreen(viewModel: MainViewModel, navController: NavController) {
    val mainColor by viewModel.colorState.collectAsState()

    Scaffold(
        topBar = {
            SettingScreenAppBar("화면 설정", mainColor, navController = navController)
        },
        modifier = Modifier.background(colorResource(id = R.color.settingScreenBackgroundColor))
    ) {
        Column {
            SettingTitleField(text = "컬러 모드")
            SettingSwitch(
                "블루",
                isChecked = mainColor == R.color.mainColor,
                onCheckedChange = { viewModel.updateColor(ColorOption.BLUE) },
                colorResId = R.color.mainColor
            )

            Divider(
                thickness = 3.dp,
                color = colorResource(id = R.color.settingScreenBackgroundColor)
            )
            SettingSwitch(
                "베이지",
                isChecked = mainColor == R.color.brownMainColor,
                onCheckedChange = { viewModel.updateColor(ColorOption.BROWN) },
                colorResId = R.color.brownMainColor
            )

            Divider(
                thickness = 3.dp,
                color = colorResource(id = R.color.settingScreenBackgroundColor)
            )
            SettingSwitch(
                "핑크",
                isChecked = mainColor == R.color.pinkMainColor,
                onCheckedChange = { viewModel.updateColor(ColorOption.PINK) },
                colorResId = R.color.pinkMainColor
            )
            Divider(
                thickness = 3.dp,
                color = colorResource(id = R.color.settingScreenBackgroundColor)
            )

            SettingSwitch(
                "연두",
                isChecked = mainColor == R.color.greenMainColor,
                onCheckedChange = { viewModel.updateColor(ColorOption.GREEN) },
                colorResId = R.color.greenMainColor
            )
        }
    }
}

@Composable
fun SettingSwitch(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colorResId: Int
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight / 17).dp)
            .padding(
                top = 10.dp,
                end = 10.dp,
                start = 15.dp,
                bottom = 10.dp
            )
    ) {
        Text(
            text,
            color = colorResource(id = R.color.settingScreenColorTextColor),
            style = switchText
        )
        Switch(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White, checkedTrackColor = colorResource(
                    id = colorResId
                ),
                uncheckedTrackColor = colorResource(id = R.color.settingScreenSwitchColor),
                uncheckedThumbColor = Color.White,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}