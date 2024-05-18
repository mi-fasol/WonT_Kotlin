package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.theme.ColorOption
import com.example.haemo_kotlin.util.BackArrowAppBar
import com.example.haemo_kotlin.viewModel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ThemeChangeScreen(viewModel: MainViewModel, navController: NavController) {
    val mainColor by viewModel.mainColor.collectAsState()

    Scaffold(
        topBar = {
            BackArrowAppBar(appBarText = "테마 변경", navController = navController)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ColorSwitch(
                isChecked = mainColor == ColorOption.BLUE,
                onCheckedChange = { viewModel.updateColor(ColorOption.BLUE) },
                colorResId = R.color.mainColor
            )

            ColorSwitch(
                isChecked = mainColor == ColorOption.PINK,
                onCheckedChange = { viewModel.updateColor(ColorOption.PINK) },
                colorResId = R.color.pinkMainColor
            )

            ColorSwitch(
                isChecked = mainColor == ColorOption.BROWN,
                onCheckedChange = { viewModel.updateColor(ColorOption.BROWN) },
                colorResId = R.color.brownMainColor
            )

            ColorSwitch(
                isChecked = mainColor == ColorOption.GREEN,
                onCheckedChange = { viewModel.updateColor(ColorOption.GREEN) },
                colorResId = R.color.greenMainColor
            )
        }

    }

}

@Composable
fun ColorSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colorResId: Int
) {
    Switch(
        checked = isChecked,
        onCheckedChange = { onCheckedChange(it) },
        colors = SwitchDefaults.colors(checkedThumbColor = colorResource(id = colorResId))
    )
}