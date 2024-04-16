package com.example.haemo_kotlin.screen.main.board

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.comment.club.ClubCommentResponseModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.userMyPageImageList
import com.example.haemo_kotlin.viewModel.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.CommentViewModel

@Composable
fun ClubPostDetailScreen(
    pId: Int,
    postViewModel: ClubPostViewModel,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val post = postViewModel.clubPost.collectAsState().value
    val user = postViewModel.user.collectAsState().value
    val postState = postViewModel.clubPostState.collectAsState().value

    LaunchedEffect(post) {
        postViewModel.getOneClubPost(pId)
        postViewModel.getClubPostingUser(pId)
        commentViewModel.getClubCommentListByPId(pId)
        commentViewModel.getCommentUser(pId)
    }

    Scaffold(
        topBar = {
            PostDetailAppBar(navController)
        },
        bottomBar = {
            SendReply(
                type = "댓글",
                postType = 1,
                value = commentViewModel.content.collectAsState().value,
                onValueChange = { newValue ->
                    commentViewModel.content.value = newValue
                })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = R.color.mainColor))
            when (postState) {
                is Resource.Error<ClubPostResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<ClubPostResponseModel> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    if (user != null && post != null) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            PostUserInfo(user, post.date)
                            ClubPostInfo(post)
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                            ClubCommentWidget(
                                type = 2,
                                pId = pId,
                                commentViewModel = commentViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubPostInfo(post: ClubPostResponseModel) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            post.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = colorResource(
                id = R.color.mainTextColor
            )
        )
        Spacer(Modifier.height(5.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight / 3.5).dp)
                .clip(RoundedCornerShape(15.dp)),
            elevation = 0.dp,
        ) {
            if (post.image == null) {
                Image(
                    painter = painterResource(id = R.drawable.dummy_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Unspecified, RoundedCornerShape(15.dp))
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = post.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Unspecified, RoundedCornerShape(15.dp))
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                post.content, fontSize = 13.sp, color = colorResource(id = R.color.mainTextColor),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@Composable
fun ClubCommentWidget(type: Int, pId: Int, commentViewModel: CommentViewModel) {
    val commentList = commentViewModel.clubCommentList.collectAsState().value
    val userList = commentViewModel.clubUserList.collectAsState().value
    LaunchedEffect(commentList) {
        commentViewModel.getClubCommentListByPId(pId)
    }
    LaunchedEffect(true) {
        commentViewModel.getClubCommentUser(pId)
    }
    Column {
        Row {
            Text("댓글", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xff040404))
            Spacer(Modifier.width(5.dp))
            Text(
                commentList.size.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.mainColor)
            )
        }
        if (commentList.isNotEmpty()) {
            commentList.forEachIndexed { index, comment ->
                if (userList.isEmpty()) {
                    Box {
                        Log.d("미란", "바박스스")
                    }
                } else {
                    ClubCommentWidgetItem(comment, userList[index])
                }
                Divider()
            }
        }
    }
}

@Composable
fun ClubCommentWidgetItem(comment: ClubCommentResponseModel, user: UserResponseModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Box(Modifier.padding(horizontal = 20.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            IconButton(onClick = { /* Handle icon button click */ }) {
                Icon(
                    painter = painterResource(id = userMyPageImageList[user.userImage]),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size((screenHeight / 18).dp)
                )
            }
            Column() {
                Text(
                    text = comment.nickname,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.mainTextColor),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    comment.content,
                    fontSize = 12.5.sp,
                    color = colorResource(id = R.color.mainTextColor),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}

//@Composable
//fun ClubCommentWidget(type: Int, pId: Int, commentViewModel: CommentViewModel) {
//    val commentList = commentViewModel.clubCommentList.collectAsState().value
//    val userList = commentViewModel.clubUserList.collectAsState().value
//    LaunchedEffect(commentList) {
//        commentViewModel.getClubCommentListByPId(pId)
//    }
//    LaunchedEffect(true) {
//        commentViewModel.getClubCommentUser(pId)
//    }
//    Column() {
//        Row() {
//            Text("댓글", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xff040404))
//            Spacer(Modifier.width(5.dp))
//            Text(
//                commentList.size.toString(),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                color = colorResource(
//                    id = R.color.mainColor
//                )
//            )
//        }
//        if (commentList.isNotEmpty()) {
//            LazyColumn {
//                items(commentList.size) { idx ->
//                    if (userList.isEmpty()) {
//                        Box() {}
//                        Log.d("미란", "바박스스")
//                    } else {
//                        ClubCommentWidgetItem(
//                            commentList[idx],
//                            userList[idx]
//                        )
//                    }
//                    Divider()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ClubCommentWidgetItem(comment: ClubCommentResponseModel, user: UserResponseModel) {
//    val config = LocalConfiguration.current
//    val screenWidth = config.screenWidthDp
//    val screenHeight = config.screenHeightDp
//    Box(
//        Modifier.padding(horizontal = 20.dp)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//                .fillMaxHeight(),
//        ) {
//            IconButton(
//                onClick = {
//
//                }
//            ) {
//                Icon(
//                    painter = painterResource(id = userMyPageImageList[user.userImage]),
//                    contentDescription = null,
//                    tint = Color.Unspecified,
//                    modifier = Modifier.size((screenHeight / 18).dp)
//                )
//            }
//            Column() {
//                Text(
//                    text = comment.nickname,
//                    fontSize = 13.5.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = colorResource(id = R.color.mainTextColor),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                )
//                Text(
//                    comment.content, fontSize = 12.5.sp,
//                    color = colorResource(id = R.color.mainTextColor),
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    maxLines = Int.MAX_VALUE
//                )
//            }
//        }
//    }
//}