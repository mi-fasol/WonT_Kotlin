package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.util.userProfileList
import com.example.haemo_kotlin.viewModel.UserViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyPageScreen(viewModel: UserViewModel, navController: NavController) {
    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    val userState = viewModel.fetchUserState.collectAsState().value

    Log.d("미란 user", user.toString())

    LaunchedEffect(true) {
        viewModel.fetchUserInfoById(context)
    }

    Scaffold(
        topBar = {
            MainPageAppBar("마이페이지", navController)
        },
        bottomBar = { MainBottomNavigation(navController = navController) },
        modifier = Modifier.background(Color(0xfff4f4f4))
    ) {
        Column {
            Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
            when (userState) {
                is Resource.Error<UserResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<UserResponseModel> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    if (user == null) {
                        ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                    } else {
                        Column(
                            //    modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            UserProfile(user = user)
                            Divider(thickness = 1.dp, color = colorResource(id = R.color.mainColor))
                            MyPageList(user.uId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfile(user: UserResponseModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height((screenHeight / 2.3).dp)
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "프로필",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff818181)
            )
            Image(
                painter = painterResource(id = userProfileList[user.userImage]),
                contentDescription = null,
                Modifier.size((screenWidth / 2.5).dp)
            )
            Text(text = user.major, fontSize = 13.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gender_man),
                    contentDescription = null,
                    Modifier.size((screenWidth / 20).dp)
                )
                Text(user.nickname, fontSize = 22.sp)
            }
        }
    }
}

@Composable
fun MyPageList(uId: Int) {
    Box(
        Modifier.background(Color(0xfff4f4f4)).fillMaxHeight()
    ){
        LazyColumn(
            Modifier.padding(bottom = 10.dp)
        ) {
            items(4) { idx ->
                MyPageListItem(idx)
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}


@Composable
fun MyPageListItem(idx: Int) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val textList = listOf<String>("내가 작성한 글", "찜한 장소", "가고 싶은 모임", "가고 싶은 소모임")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height((screenHeight / 16).dp)
            .clickable { }) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = textList[idx], fontSize = 16.5.sp, color = Color(0xff515151))
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorResource(
                    id = R.color.mainColor
                )
            )
        }
    }
}
