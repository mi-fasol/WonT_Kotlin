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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R

@Composable
fun MainBottomNavigation(navController: NavController, onItemSelected: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp

    val currentRoute = navController.currentDestination?.route ?: ""

    val selectedItem: MutableState<String> = remember { mutableStateOf(currentRoute) }

    val onNavigationItemSelected: (String) -> Unit = { route ->
        selectedItem.value = route
        onItemSelected(route)
    }

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
            selected = selectedItem.value == "mainScreen",
            onClick = {
//                navController.navigate(NavigationRoutes.MainScreen.route) {
//                    popUpTo(NavigationRoutes.MainScreen.route) {
//                        inclusive = true
//                    }
//                }
                onNavigationItemSelected("mainScreen")
                onItemSelected("mainScreen")
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
            selected = selectedItem.value == "clubScreen",
            onClick = {
                onNavigationItemSelected("clubScreen")
                onItemSelected("clubScreen")
//                navController.navigate(NavigationRoutes.ClubScreen.route) {
//                    popUpTo(NavigationRoutes.MainScreen.route) {
//                        inclusive = true
//                    }
//                }
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
            selected = selectedItem.value == "hotPlaceScreen",
            onClick = {
                onNavigationItemSelected("hotPlaceScreen")
                onItemSelected("hotPlaceScreen")
//                navController.navigate(NavigationRoutes.HotPlaceScreen.route) {
//                    popUpTo(NavigationRoutes.MainScreen.route) {
//                        inclusive = true
//                    }
//                }
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
        BottomNavigationItem(
            icon = {
                val isSelected = selectedItem.value == "myPageScreen"
                val borderColor = if (isSelected) R.color.mainColor else R.color.mainGreyColor
                Icon(
                    painter = painterResource(id = userMyPageImageList[SharedPreferenceUtil(context).getInt("image", 0)]),
                    contentDescription = null,
                    modifier = Modifier
                        .size((screenWidth / 15).dp)
                        .border(
                            width = 1.dp,
                            colorResource(borderColor),
                            shape = CircleShape
                        ),
                    tint = Color.Unspecified
                )
            },
            selected = selectedItem.value == "myPageScreen",
            onClick = {
                onNavigationItemSelected ("myPageScreen")
                onItemSelected("myPageScreen")
            },
            selectedContentColor = colorResource(id = R.color.mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
    }
}