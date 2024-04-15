package com.example.haemo_kotlin.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.unit.toSize
import com.example.haemo_kotlin.R
import java.time.format.TextStyle

@Composable
fun TitleEnterField(type: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            type,
            fontSize = 17.5.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.mainColor),
            modifier = Modifier.weight(1f)
        )
        Column(
            Modifier.weight(6f)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = colorResource(id = R.color.postRegisterTextColor),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Divider()
        }
    }
}

@Composable
fun PostSelectDropDownMenu(
    text: String,
    list: List<String>,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(text) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val borderColor =
        if (expanded) colorResource(id = R.color.mainColor) else colorResource(id = R.color.postRegisterTextColor)

    Box(
        modifier = Modifier
            .border(width = 1.5.dp, borderColor, shape = RoundedCornerShape(15.dp))
            .clickable {
                expanded = true
            }
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedText, color = borderColor)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(icon, contentDescription = null, tint = borderColor)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min) // 높이를 최소 크기로 설정
                .then(
                    if (list.size > 5) {
                        Modifier.height(200.dp)
                    } else {
                        Modifier
                    }
                )
            // .verticalScroll(rememberScrollState())
        ) {
            list.forEach { label -> // Show only up to 5 items
                DropdownMenuItem(
                    onClick = {
                        selectedText = label
                        onValueChange(label)
                        expanded = false
                    },
                ) {
                    Text(text = label, fontSize = 11.sp)
                }
            }
        }

    }
}

@Composable
fun ContentEnterField(value: String, onValueChange: (String) -> Unit) {
    Box(
        Modifier.background(Color(0xfff5f5f5), RoundedCornerShape(15.dp)).fillMaxSize().padding(bottom = 30.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(10.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color(0xffb6b6b6),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
        )
    }
}