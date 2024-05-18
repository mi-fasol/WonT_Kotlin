package com.example.haemo_kotlin.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.theme.ColorOption
import com.example.haemo_kotlin.viewModel.MainViewModel

@Composable
fun ThemeChangeScreen(viewModel: MainViewModel) {
    val mainColor by viewModel.mainColor.collectAsState()

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