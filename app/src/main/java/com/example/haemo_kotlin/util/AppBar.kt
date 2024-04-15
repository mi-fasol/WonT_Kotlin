package com.example.haemo_kotlin.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackArrowAppBar(appBarText: String, navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text(text = appBarText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color(0xff545454),
            titleContentColor = Color(0xff595959),
            actionIconContentColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageAppBar(appBarText: String, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = appBarText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.mainColor)
            )
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorResource(id = R.color.mainColor)
                )
            }
        },
        elevation = 0.dp,
        backgroundColor = Color.White,
    )
}

@Composable
fun PostDetailAppBar(navController: NavController) {
    TopAppBar(
        title = {
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                tint = Color(0xff545454)
            )
        },
        actions = {
            IconButton(onClick = {
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.wish_meeting_icon),
                    contentDescription = null,
                    tint = colorResource(id = R.color.mainColor),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        elevation = 0.dp,
        backgroundColor = Color.White,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageListAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.wont),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight()
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        },
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color(0xff545454),
            titleContentColor = Color(0xff595959),
            actionIconContentColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostRegisterAppBar(appBarText: String, navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarText,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                tint = Color.White
            )
        },
        colors = TopAppBarColors(
            containerColor = colorResource(id = R.color.mainColor),
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.Transparent
        )
    )
}