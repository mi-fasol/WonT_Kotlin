package com.example.haemo_kotlin.screen.setting

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.screen.main.board.CommentWidget
import com.example.haemo_kotlin.screen.main.board.MeetingBoardItem
import com.example.haemo_kotlin.screen.main.board.PostInfo
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.CommentViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel

@Composable
fun MyMeetingBoardScreen(
    postViewModel: PostViewModel,
    navController: NavController,
    nickname: String,
) {
    val post = postViewModel.postModelList.collectAsState().value
    val postState = postViewModel.postModelListState.collectAsState().value


    LaunchedEffect(post) {
        postViewModel.getPost()
    }

    Scaffold(
        topBar = {
            MyPageListAppBar(navController)
        },
//        bottomBar = {
//            MainBottomNavigation(navController = navController)
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = R.color.mainColor))
            when (postState) {
                is Resource.Error<List<PostResponseModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<PostResponseModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    when (post.size) {
                        0 -> {
                            ErrorScreen("등록한 글이 아직 없어요!")
                        }

                        else -> {
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
                                MyMeetingBoardList(post, nickname, postViewModel, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyMeetingBoardList(
    postList: List<PostResponseModel>, nickname: String, postViewModel: PostViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val list = postList.filter {
        it.nickname == nickname
    }
    Column {
        list.forEachIndexed { _, post ->
            MyMeetingBoardItem(post, postViewModel, navController)
        }
    }
}

@Composable
fun MyMeetingBoardItem(
    post: PostResponseModel,
    viewModel: PostViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
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
                    "3/${post.person}", fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff82C0EA),
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
//                    fontWeight = FontWeight.SemiBold,
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