package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.myPageListItem
import com.example.haemo_kotlin.ui.theme.myPageNickname
import com.example.haemo_kotlin.ui.theme.myPageProfile
import com.example.haemo_kotlin.ui.theme.withdrawCheck
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MyPageAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.userProfileList
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.UserViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyPageScreen(
    viewModel: UserViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    val userState = viewModel.fetchUserState.collectAsState().value
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)
    Log.d("미란 user", user.toString())

    LaunchedEffect(true) {
        viewModel.fetchUserInfoById(uId)
    }

    Scaffold(
        topBar = {
            MyPageAppBar("마이페이지", mainColor, navController)
        },
        modifier = Modifier.background(Color(0xfff4f4f4))
    ) { innerPadding ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
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
                                Divider(
                                    thickness = 1.dp,
                                    color = colorResource(mainColor)
                                )
                                MyPageList(
                                    mainColor,
                                    user.nickname,
                                    mainViewModel,
                                    navController
                                )
                            }
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
                style = myPageProfile,
                color = Color(0xff818181)
            )
            Image(
                painter = painterResource(id = userProfileList[user.userImage]),
                contentDescription = null,
                Modifier.size((screenWidth / 2.5).dp)
            )
            Text(text = user.major, style = withdrawCheck)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gender_man),
                    contentDescription = null,
                    Modifier.size((screenWidth / 20).dp)
                )
                Text(user.nickname, style = myPageNickname)
            }
        }
    }
}

@Composable
fun MyPageList(
    mainColor: Int,
    nickname: String,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    Box(
        Modifier
            .background(Color(0xfff4f4f4))
            .fillMaxHeight()
    ) {
        LazyColumn(
            Modifier.padding(bottom = 10.dp)
        ) {
            items(4) { idx ->
                MyPageListItem(idx, mainColor, nickname, mainViewModel, navController)
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}


@Composable
fun MyPageListItem(
    idx: Int,
    mainColor: Int,
    nickname: String,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val textList = listOf("내가 작성한 글", "찜한 장소", "가고 싶은 모임", "가고 싶은 소모임")
    val navigationRoutes = listOf(
        NavigationRoutes.MyMeetingBoardScreen.createRoute(nickname),
        NavigationRoutes.MyWishHotPlaceScreen.route,
        NavigationRoutes.MyWishMeetingScreen.route,
        NavigationRoutes.MyWishClubScreen.route
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(Color.White)
            .padding(top = 3.dp)
            .fillMaxWidth()
            .height((screenHeight / 16).dp)
            .clickable {
                mainViewModel.beforeStack.value = "myPageScreen"
                navController.navigate(navigationRoutes[idx])
            }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = textList[idx],
                color = colorResource(id = R.color.mainGreyColor),
                style = myPageListItem
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorResource(
                    id = mainColor
                )
            )
        }
    }
}
