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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.user.Role
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes

@Composable
fun MainFloatingButton(navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)
    val user = SharedPreferenceUtil(context).getUser()
    val userFloatList = listOf("모임 등록", "소모임 등록", "핫플 등록")
    val userNavFloatList = listOf(
        NavigationRoutes.PostRegisterScreen.route,
        NavigationRoutes.ClubPostRegisterScreen.route,
        NavigationRoutes.HotPlacePostRegisterScreen.route
    )

    Column(horizontalAlignment = Alignment.End) {
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp)
            ) {
                if (user.role != Role.ADMIN) {
                    userFloatList.forEach {
                        FabItem(
                            title = it,
                            mainColor = mainColor,
                            onClicked = {
                                navController.navigate(userNavFloatList[userFloatList.indexOf(it)])
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    FabItem(title = "공지 작성", mainColor = mainColor) {
                        navController.navigate(NavigationRoutes.NoticeRegisterScreen.route)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            shape = CircleShape,
            backgroundColor = colorResource(id = mainColor)
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
fun FabItem(title: String, mainColor: Int, onClicked: () -> Unit) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width((screenWidth / 4.5).dp)
            .clickable { onClicked() }
            .background(colorResource(id = mainColor), RoundedCornerShape(10.dp))
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