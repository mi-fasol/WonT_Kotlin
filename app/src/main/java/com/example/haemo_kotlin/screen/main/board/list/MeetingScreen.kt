package com.example.haemo_kotlin.screen.main.board.list

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.board.PostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MeetingScreen(postViewModel: PostViewModel, navController: NavController) {
    val postList = postViewModel.postModelList.collectAsState().value
    val todayPostList = postViewModel.todayPostList.collectAsState().value
    val postListState = postViewModel.postModelListState.collectAsState().value

    LaunchedEffect(postList) {
        postViewModel.getPost()
    }
    LaunchedEffect(todayPostList) {
        postViewModel.getTodayPost()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("친구 구하는 곳", navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
            when (postListState) {
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
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Today24HoursBoard(todayPostList, postViewModel, navController)
                        MeetingBoard(
                            postList = postList,
                            viewModel = postViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Today24HoursBoard(
    postList: List<PostResponseModel>,
    viewModel: PostViewModel,
    navController: NavController
) {
    when (postList.size) {
        0 -> {
            Box {}
        }

        else -> {
            Column(
                Modifier.padding(top = 15.dp, bottom = 10.dp)
            ) {
                Text("공지 24시간", fontSize = 13.sp, color = Color(0xff393939))
                LazyRow {
                    items(postList.size) { idx ->
                        TodayNotice(postList[idx], viewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun TodayNotice(post: PostResponseModel, viewModel: PostViewModel, navController: NavController) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Box(
        modifier = Modifier
            .blur(30.dp)
    ) {
        Card(
            Modifier
                .padding(top = 5.dp, bottom = 15.dp, end = 10.dp)
                .height((screenHeight / 5.5).dp)
                .clickable {
                    navController.navigate(NavigationRoutes.MeetingPostDetailScreen.createRoute(post.pId))
                }
                .width((screenWidth / 3).dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color(0xff82C0EA))
        ) {
            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = post.title, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text("${post.person}명", fontSize = 12.sp, color = Color(0xff595959))
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            convertDate(post.deadline),
                            fontSize = 12.sp,
                            color = Color(0xff595959),
                            modifier = Modifier.weight(8f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingBoard(
    postList: List<PostResponseModel>,
    viewModel: PostViewModel,
    navController: NavController
) {
    when (postList.size) {
        0 -> {
            ErrorScreen("등록된 글이 아직 없어요!")
        }

        else -> {
            LazyColumn {
                items(postList.size) { idx ->
                    MeetingBoardItem(postList[idx], viewModel, navController)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun MeetingBoardItem(
    post: PostResponseModel,
    viewModel: PostViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val date = convertDate(post.date)
    Box(
        modifier = Modifier
            .height((screenHeight / 10).dp)
            .clickable {
                navController.navigate(NavigationRoutes.MeetingPostDetailScreen.createRoute(post.pId))
            }
    ) {
        Column(
            Modifier
                .padding(vertical = 13.dp, horizontal = 3.dp)
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
                    date, fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff595959)
                )
            }
        }
    }
}