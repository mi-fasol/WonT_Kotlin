package com.example.haemo_kotlin.screen.main.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.haemo_kotlin.screen.main.board.list.ClubScreen
import com.example.haemo_kotlin.screen.main.board.list.HotPlaceScreen
import com.example.haemo_kotlin.screen.main.board.list.MeetingScreen
import com.example.haemo_kotlin.screen.setting.MyPageScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MainFloatingButton
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.UserViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel


@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    postViewModel: PostViewModel,
    wishViewModel : WishViewModel,
    clubPostViewModel: ClubPostViewModel,
    hotPostViewModel: HotPlacePostViewModel,
    userViewModel: UserViewModel
) {
    val selectedItem = mainViewModel.beforeStack.collectAsState().value

    val onItemSelected: (String) -> Unit = { item ->
        mainViewModel.beforeStack.value = item
    }

    val context = LocalContext.current
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)
    val nickname = SharedPreferenceUtil(context).getString("nickname", "")

    if(nickname.isNullOrBlank()){
        LaunchedEffect(true){

        }
    }


    LaunchedEffect(true) {
        userViewModel.fetchUserInfoById(uId)
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController = navController, onItemSelected = onItemSelected, mainViewModel)
        },
        floatingActionButton = { MainFloatingButton(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                "mainScreen" -> {
                    MeetingScreen(
                        postViewModel = postViewModel,
                        mainViewModel,
                        navController = navController
                    )
                }
                "clubScreen" -> {
                    ClubScreen(
                        postViewModel = clubPostViewModel,
                        mainViewModel,
                        navController = navController
                    )
                }

                "hotPlaceScreen" -> {
                    HotPlaceScreen(
                        postViewModel = hotPostViewModel,
                        wishViewModel,
                        mainViewModel,
                        navController = navController
                    )
                }

                "myPageScreen" -> {
                    MyPageScreen(
                        viewModel = userViewModel,
                        mainViewModel,
                        navController = navController
                    )
                }

                else -> {
                    MeetingScreen(
                        postViewModel = postViewModel,
                        mainViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}