package com.example.haemo_kotlin.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R

@Composable
fun PostRegisterFloatingButton(navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.End) {
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp)
            ) {
                FabItem(
                    title = "모임 등록",
                    onClicked = {
                        navController.navigate(NavigationRoutes.PostRegisterScreen.route)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                FabItem(
                    title = "소모임 등록",
                    onClicked = {
                        navController.navigate(NavigationRoutes.ClubPostRegisterScreen.route)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                FabItem(
                    title = "핫플 등록",
                    onClicked = {
                        navController.navigate(NavigationRoutes.HotPlacePostRegisterScreen.route)
                    }
                )
            }
        }
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            shape = CircleShape,
            backgroundColor = colorResource(id = R.color.mainColor)
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                tint = Color.White,
                contentDescription = "This is Expand Button"
            )
        }
    }
}


@Composable
fun FabItem(title: String, onClicked: () -> Unit) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp
    val screenHeight = conf.screenHeightDp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((screenWidth / 4.5).dp)
            .clickable { onClicked() }
            .background(colorResource(id = R.color.mainColor), RoundedCornerShape(10.dp))
    ) {
        Text(
            title,
            fontSize = 11.5.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}