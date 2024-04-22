package com.example.haemo_kotlin.screen.main.board.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.comment.CommentResponseModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.UserBottomSheet
import com.example.haemo_kotlin.util.userMyPageImageList
import com.example.haemo_kotlin.viewModel.CommentViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel

@Composable
fun MeetingPostDetailScreen(
    pId: Int,
    postViewModel: PostViewModel,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val post = postViewModel.postModel.collectAsState().value
    val user = postViewModel.user.collectAsState().value
    val postState = postViewModel.postModelState.collectAsState().value
    val accept = postViewModel.acceptationList.collectAsState().value
    val commentList = commentViewModel.commentList.collectAsState().value
    val content = commentViewModel.content.collectAsState().value

    LaunchedEffect(commentList) {
        postViewModel.getOnePost(pId)
        postViewModel.getPostingUser(pId)
        postViewModel.getAcceptationUserByPId(pId)
        postViewModel.getCommentListByPId(pId)
    }

    Scaffold(
        topBar = {
            PostDetailAppBar(navController)
        },
        bottomBar = {
            SendReply(
                type = "댓글",
                postType = 1,
                pId = pId,
                value = content,
                commentViewModel = commentViewModel,
                onValueChange = { newValue ->
                    commentViewModel.content.value = newValue
                }){
                commentViewModel.registerComment(content, pId, 1)
                commentViewModel.content.value = ""
            }
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
                is Resource.Error<PostResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<PostResponseModel> -> {
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
                            PostUserInfo(user, post.date, navController)
                            PostInfo(post, accept)
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                            CommentWidget(
                                type = 1,
                                pId = pId,
                                commentViewModel = commentViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostInfo(post: PostResponseModel, accept: List<AcceptationResponseModel>) {
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
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(post.content, fontSize = 13.sp, color = colorResource(id = R.color.mainTextColor))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                "${accept.size}/${post.person}",
                color = colorResource(id = R.color.mainColor),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height((screenHeight / 26).dp)
                    .width((screenWidth / 5).dp)
                    .background(
                        colorResource(id = R.color.mainColor),
                        RoundedCornerShape(23.dp)
                    )
                    .clickable { },
            ) {
                Text(
                    "참여완료",
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CommentWidget(
    type: Int,
    pId: Int,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val commentList = commentViewModel.commentList.collectAsState().value
    val userList = commentViewModel.userList.collectAsState().value
    LaunchedEffect(commentList) {
        commentViewModel.getCommentListByPId(pId, type)
        commentViewModel.getCommentUser(pId, type)
    }
    Column() {
        Row() {
            Text("댓글", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xff040404))
            Spacer(Modifier.width(5.dp))
            Text(
                commentList.size.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(
                    id = R.color.mainColor
                )
            )
        }
        if (commentList.isNotEmpty()) {
            commentList.forEachIndexed { index, comment ->
                userList.getOrNull(index)?.let { CommentWidgetItem(comment, it, navController) }
                Divider()
            }
        }
    }
}

@Composable
fun CommentWidgetItem(
    comment: CommentResponseModel,
    user: UserResponseModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    var bottomSheetOpen by remember { mutableStateOf(false) }

    Box(
        Modifier.padding(horizontal = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(),
        ) {
            IconButton(
                onClick = {
                    bottomSheetOpen = true
                }
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    comment.content, fontSize = 12.5.sp,
                    color = colorResource(id = R.color.mainTextColor),
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }

    if (bottomSheetOpen) {
        UserBottomSheet(user = user, navController = navController) {
            bottomSheetOpen = false
        }
    }
}