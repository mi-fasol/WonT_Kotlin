package com.example.haemo_kotlin.screen.main.board

import android.util.Log
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
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel
import com.example.haemo_kotlin.viewModel.UserViewModel


@Composable
fun MainScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    clubPostViewModel: ClubPostViewModel,
    hotPostViewModel: HotPlacePostViewModel,
    userViewModel: UserViewModel
) {
    var selectedItem by remember { mutableStateOf("mainScreen") }

    val onItemSelected: (String) -> Unit = { item ->
        selectedItem = item
    }

    val context = LocalContext.current
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)

    LaunchedEffect(true) {
        userViewModel.fetchUserInfoById(uId)
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController = navController, onItemSelected = onItemSelected)
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
                    MeetingScreen(postViewModel = postViewModel, navController = navController)
                }

                "clubScreen" -> {
                    ClubScreen(postViewModel = clubPostViewModel, navController = navController)
                }

                "hotPlaceScreen" -> {
                    HotPlaceScreen(postViewModel = hotPostViewModel, navController = navController)
                }

                "myPageScreen" -> {
                    MyPageScreen(viewModel = userViewModel, navController = navController)
                }

                else -> {
                    MeetingScreen(postViewModel = postViewModel, navController = navController)
                }
            }
            //MeetingScreen(postViewModel = postViewModel, navController = navController)
        }
    }
}