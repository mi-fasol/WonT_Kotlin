package com.example.haemo_kotlin.screen.setting.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.meetingScreenDeadline
import com.example.haemo_kotlin.ui.theme.meetingScreenPerson
import com.example.haemo_kotlin.ui.theme.meetingScreenTitle
import com.example.haemo_kotlin.ui.theme.myWishInfo
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.WishButton
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel

@Composable
fun MyWishMeetingScreen(
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val post = wishViewModel.wishMeetingList.collectAsState().value
    val postState = wishViewModel.postModelListState.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(post) {
        wishViewModel.getWishMeeting()
    }

    Scaffold(
        topBar = {
            MyPageListAppBar(mainColor, navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = mainColor))
            when (postState) {
                is Resource.Error<List<PostResponseModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<PostResponseModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorResource(id = mainColor))
                    }
                }

                else -> {
                    when (post.size) {
                        0 ->
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ErrorScreen("찜한 모임이 아직 없어요!")
                            }

                        else ->
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    "가고 싶은 모임",
                                    style = myWishInfo,
                                    color = colorResource(
                                        id = R.color.myBoardColor
                                    ),
                                    modifier = Modifier.padding(vertical = 15.dp)
                                )
                                Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                                MyWishMeetingList(post, mainColor, wishViewModel, navController)
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWishMeetingList(
    postList: List<PostResponseModel>, mainColor: Int, viewModel: WishViewModel,
    navController: NavController
) {
    Column {
        postList.forEachIndexed { _, post ->
            MyWishMeetingItem(post, viewModel, mainColor, navController)
        }
    }
}

@Composable
fun MyWishMeetingItem(
    post: PostResponseModel,
    viewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    val date = convertDate(post.date)
    Box(
        modifier = Modifier
            .clickable {
                navController.navigate(NavigationRoutes.MeetingPostDetailScreen.createRoute(post.pId))
            }
            .padding(top = 10.dp)
            .border(width = 1.dp, color = Color(0xffd9d9d9), shape = RoundedCornerShape(15.dp))
    ) {
        Column(
            Modifier
                .padding(vertical = 15.dp, horizontal = 10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = post.title,
                    style = meetingScreenTitle,
                    color = colorResource(id = R.color.mainGreyColor)
                )
                WishButton(
                    post = post,
                    clubPost = null,
                    hotPlacePost = null,
                    mainColor = mainColor,
                    type = 1,
                    wishViewModel = viewModel
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${post.person}명",
                    style = meetingScreenPerson,
                    color = colorResource(id = R.color.mainGreyColor)
                )
                Text(
                    date,
                    style = meetingScreenDeadline,
                    color = colorResource(id = R.color.mainGreyColor)
                )
            }
        }
    }
}