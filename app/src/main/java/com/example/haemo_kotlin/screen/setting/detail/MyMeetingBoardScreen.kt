package com.example.haemo_kotlin.screen.setting.detail

import androidx.compose.foundation.background
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
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.AcceptationViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel

@Composable
fun MyMeetingBoardScreen(
    postViewModel: PostViewModel,
    acceptationViewModel: AcceptationViewModel,
    mainViewModel: MainViewModel,
    navController: NavController,
    nickname: String,
) {
    val post = postViewModel.postModelList.collectAsState().value
    val postState = postViewModel.postModelListState.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(post) {
        postViewModel.getPost()
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
                        0 -> {
                            ErrorScreen("등록한 글이 아직 없어요!")
                        }

                        else -> {
                            MyMeetingBoardList(
                                post,
                                nickname,
                                mainColor,
                                acceptationViewModel,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyMeetingBoardList(
    postList: List<PostResponseModel>,
    nickname: String,
    mainColor: Int,
    viewModel: AcceptationViewModel,
    navController: NavController
) {
    val list = postList.filter {
        it.nickname == nickname
    }
    if (list.isEmpty()) {
        Box(Modifier.background(Color.White), contentAlignment = Alignment.Center) {
            ErrorScreen(text = "작성한 글이 없어요!")
        }
    } else
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            Text(
                "내가 작성한 글",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(
                    id = R.color.myBoardColor
                ),
                modifier = Modifier.padding(vertical = 15.dp)
            )
            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
            list.forEachIndexed { _, post ->
                MyMeetingBoardItem(post, viewModel, mainColor, navController)
            }
        }
}

@Composable
fun MyMeetingBoardItem(
    post: PostResponseModel,
    viewModel: AcceptationViewModel,
    mainColor: Int,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val attendees by viewModel.attendeeList.collectAsState()
    val acceptationList by viewModel.attendeeModelList.collectAsState()
    val allowedUser = acceptationList[post.pId]?.filter { it.acceptation }?.size ?: 0

    LaunchedEffect(Unit){
        viewModel.getAcceptationByPId(post.pId)
        viewModel.getAttendeeByPId(post.pId)
    }

    Box(
        modifier = Modifier
            .height((screenHeight / 9).dp)
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
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff595959),
                    modifier = Modifier
                        .weight(10f)
                        .fillMaxWidth()
                )
                Text(
                    "$allowedUser/${post.person}", fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = mainColor),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${post.person}명",
                    fontSize = 11.5.sp,
                    color = Color(0xff999999)
                )
                Text(
                    convertDate(post.date), fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff595959)
                )
            }
        }
    }
}