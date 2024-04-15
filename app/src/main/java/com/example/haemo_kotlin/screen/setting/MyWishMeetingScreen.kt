package com.example.haemo_kotlin.screen.setting

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.WishViewModel

@Composable
fun MyWishMeetingScreen(
    wishViewModel: WishViewModel,
    navController: NavController,
    uId: Int,
) {
    val post = wishViewModel.wishMeetingList.collectAsState().value
    val postState = wishViewModel.postModelListState.collectAsState().value


    LaunchedEffect(post) {
        wishViewModel.getWishMeeting(uId)
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
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(
                                        id = R.color.myBoardColor
                                    ),
                                    modifier = Modifier.padding(vertical = 15.dp)
                                )
                                Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                                MyWishMeetingList(post, uId, wishViewModel, navController)
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWishMeetingList(
    postList: List<PostResponseModel>, uId: Int, viewModel: WishViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    Column {
        postList.forEachIndexed { _, post ->
            MyWishMeetingItem(post, viewModel, navController)
        }
    }
}

@Composable
fun MyWishMeetingItem(
    post: PostResponseModel,
    viewModel: WishViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val date = convertDate(post.date)
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
                Icon(
                    painter = painterResource(id = R.drawable.wish_meeting_icon),
                    tint = colorResource(
                        id = R.color.mainColor
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
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