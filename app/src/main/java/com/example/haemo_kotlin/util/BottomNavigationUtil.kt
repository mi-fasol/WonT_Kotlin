package com.example.haemo_kotlin.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.viewModel.MainViewModel

@Composable
fun MainBottomNavigation(
    navController: NavController,
    onItemSelected: (String) -> Unit,
    viewModel: MainViewModel
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp

    val selectedItem = viewModel.beforeStack.collectAsState().value
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)

    val onNavigationItemSelected: (String) -> Unit = { route ->
        viewModel.beforeStack.value = route
        onItemSelected(route)
    }

    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.border(
            1.dp,
            color = colorResource(id = mainColor),
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
            selected = selectedItem == "mainScreen",
            onClick = {
                onNavigationItemSelected("mainScreen")
                onItemSelected("mainScreen")
            },
            selectedContentColor = colorResource(id = mainColor),
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
            selected = selectedItem == "clubScreen",
            onClick = {
                onNavigationItemSelected("clubScreen")
                onItemSelected("clubScreen")
            },
            selectedContentColor = colorResource(id = mainColor),
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
            selected = selectedItem == "hotPlaceScreen",
            onClick = {
                onNavigationItemSelected("hotPlaceScreen")
                onItemSelected("hotPlaceScreen")
            },
            selectedContentColor = colorResource(id = mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
        BottomNavigationItem(
            icon = {
                val isSelected = selectedItem == "myPageScreen"
                val borderColor = if (isSelected) mainColor else R.color.mainGreyColor
                Icon(
                    painter = painterResource(
                        id = userMyPageImageList[SharedPreferenceUtil(context).getInt("image", 0)]
                    ),
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
            selected = selectedItem == "myPageScreen",
            onClick = {
                onNavigationItemSelected("myPageScreen")
                onItemSelected("myPageScreen")
            },
            selectedContentColor = colorResource(id = mainColor),
            unselectedContentColor = colorResource(id = R.color.mainGreyColor)
        )
    }
}