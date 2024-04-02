package com.example.haemo_kotlin.util

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R

@Composable
fun MainBottomNavigation(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.border(
            1.dp,
            color = colorResource(id = R.color.mainColor),
            RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        )
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.meeting_bottom_icon),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 15).dp)
                )
            },
            selected = navController.currentDestination?.route == "mainScreen",
            onClick = {
                navController.navigate(NavigationRoutes.MainScreen.route)
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.club_bottom_icon),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 15).dp)
                )
            },
            selected = navController.currentDestination?.route == "meetingScreen",
            onClick = {
                navController.navigate(NavigationRoutes.MeetingScreen.route)
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.hotplace_bottom_icon),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 15).dp)
                )
            },
            selected = navController.currentDestination?.route == "meetingScreen",
            onClick = {
                navController.navigate(NavigationRoutes.MeetingScreen.route)
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
        BottomNavigationItem(
            icon = {
                val isSelected = navController.currentDestination?.route == "meetingScreen"
                if (isSelected) {
                    Icon(
                        painter = painterResource(id = userMyPageImageList[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .size((screenWidth / 15).dp)
                            .border(
                                width = 1.dp,
                                colorResource(R.color.mainColor),
                                shape = CircleShape
                            ),
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painter = painterResource(id = userMyPageImageList[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .size((screenWidth / 15).dp)
                            .border(
                                width = 1.dp,
                                colorResource(R.color.mainGreyColor),
                                shape = CircleShape
                            ),
                        tint = Color.Unspecified
                    )
                }
            },
            selected = navController.currentDestination?.route == "meetingScreen",
            onClick = {
                navController.navigate(NavigationRoutes.MeetingScreen.route)
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
    }
}