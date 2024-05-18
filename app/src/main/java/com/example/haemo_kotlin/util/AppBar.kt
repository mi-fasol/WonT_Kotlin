package com.example.haemo_kotlin.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackArrowAppBar(appBarText: String, navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text(text = appBarText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
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

@Composable
fun MainPageAppBar(appBarText: String, mainColor: Int, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = appBarText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(mainColor)
            )
        },
        actions = {
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.ChatListScreen.route)
            }) {
                Icon(
                    painterResource(id = R.drawable.chat_icon),
                    contentDescription = null,
                    tint = colorResource(mainColor),
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        elevation = 0.dp,
        backgroundColor = Color.White,
    )
}
@Composable
fun PostDetailAppBar(
    viewModel: CommentViewModel,
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    mainColor: Int,
    pId: Int,
    type: Int,
    navController: NavController
) {
    val isWished by wishViewModel.isWished.collectAsState()
    val postId by wishViewModel.pId.collectAsState()
    var wished by remember{ mutableStateOf(false) }
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    LaunchedEffect(Unit, key2 = isWished) {
        wishViewModel.pId.value = pId
        launch {
            wished = wishViewModel.checkIsWishedPost(postId, type)
        }
        Log.d("미란링료료", wished.toString())
    }

    val iconColor =
        if (wished) colorResource(mainColor) else colorResource(id = R.color.postRegisterTextColor)

    val icon =
        if (type == 3) painterResource(id = R.drawable.heart_icon) else painterResource(id = R.drawable.wish_meeting_icon)
    val coroutineScope = rememberCoroutineScope()

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
                        viewModel.isReply.value = false
                        viewModel.commentId.value = 0
                        wishViewModel.pId.value = 0
                        navController.popBackStack()
                        mainViewModel.beforeStack.value = when (type) {
                            1 -> "mainScreen"
                            2 -> "clubScreen"
                            else -> "hotPlaceScreen"
                        }
                    },
                tint = Color(0xff545454)
            )
        },
        actions = {
            IconButton(onClick = {
                coroutineScope.launch {
                    if (!wished) {
                        wishViewModel.addWishList(pId, type)
                    } else {
                        wishViewModel.deleteWishList(pId, type)
                    }
                    wished = !wished
                }
            },
                ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .size((screenWidth / 20).dp)

                )
            }
        },
        elevation = 0.dp,
        backgroundColor = Color.White,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageListAppBar(mainColor: Int, navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Icon(
                painter = painterResource(id = R.drawable.wont),
                contentDescription = null,
                tint = colorResource(id = mainColor),
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
fun PostRegisterAppBar(appBarText: String, mainColor: Int, navController: NavController) {
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
            containerColor = colorResource(id = mainColor),
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.Transparent
        )
    )
}

@Composable
fun ChatRoomAppBar(nickname: String, mainColor: Int, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = nickname,
                color = colorResource(id = R.color.mainTextColor),
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "뒤로 가기",
                    modifier = Modifier.size(35.dp),
                    tint = colorResource(
                        id = mainColor
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    painterResource(id = R.drawable.chat_icon),
                    contentDescription = "채팅 목록",
                    modifier = Modifier.size(30.dp),
                    tint = colorResource(
                        id = mainColor
                    )
                )
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}