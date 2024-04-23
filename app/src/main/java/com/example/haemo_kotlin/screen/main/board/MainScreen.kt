package com.example.haemo_kotlin.screen.main.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.haemo_kotlin.screen.main.board.list.ClubScreen
import com.example.haemo_kotlin.screen.main.board.list.HotPlaceScreen
import com.example.haemo_kotlin.screen.main.board.list.MeetingScreen
import com.example.haemo_kotlin.screen.setting.MyPageScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.PostRegisterFloatingButton
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.UserViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel


@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    postViewModel: PostViewModel,
    clubPostViewModel: ClubPostViewModel,
    hotPostViewModel: HotPlacePostViewModel,
    userViewModel: UserViewModel
) {
//    var selectedItem by remember { mutableStateOf("mainScreen") }
    var selectedItem = mainViewModel.beforeStack.collectAsState().value

    val onItemSelected: (String) -> Unit = { item ->
        mainViewModel.beforeStack.value = item
    }

    val context = LocalContext.current
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)

    LaunchedEffect(true) {
        userViewModel.fetchUserInfoById(uId)
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController = navController, onItemSelected = onItemSelected, mainViewModel)
        },
        floatingActionButton = { PostRegisterFloatingButton(navController) }
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