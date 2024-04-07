package com.example.haemo_kotlin.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.PostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.viewModel.PostViewModel

@Composable
fun MeetingPostDetailScreen(pId: Int, postViewModel: PostViewModel, navController: NavController) {
    val post = postViewModel.postModel.collectAsState().value
    val user = postViewModel.user.collectAsState().value
    val postState = postViewModel.postModelState.collectAsState().value

    LaunchedEffect(post) {
        postViewModel.getOnePost(pId)
        postViewModel.getPostingUser(pId)
    }

    Scaffold(
        topBar = {
            MainPageAppBar("친구 구하는 곳", navController)
        },
        bottomBar = {
            MainBottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = R.color.mainColor))
            when (postState) {
                is Resource.Error<PostModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<PostModel> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    if (user != null && post != null) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            PostUserInfo(user, post.date)
                        }
                    }
                }
            }
        }
    }
}