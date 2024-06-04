package com.example.haemo_kotlin.screen.main.board.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
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
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.ui.theme.boardDetailContent
import com.example.haemo_kotlin.ui.theme.boardDetailTitle
import com.example.haemo_kotlin.util.CommentWidget
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostManagementDialog
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.YesOrNoDialog
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel

@Composable
fun ClubPostDetailScreen(
    pId: Int,
    postViewModel: ClubPostViewModel,
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val post = postViewModel.clubPost.collectAsState().value
    val user = postViewModel.user.collectAsState().value
    val postState = postViewModel.clubPostState.collectAsState().value
    val commentList = commentViewModel.commentList.collectAsState().value
    val content = commentViewModel.content.collectAsState().value
    val isReply = commentViewModel.isReply.collectAsState().value
    val replyList = commentViewModel.replyList.collectAsState().value
    val repliedCId = commentViewModel.commentId.collectAsState().value
    val beforeCId = commentViewModel.beforeCId.collectAsState().value
    val isWished = wishViewModel.isWished.collectAsState().value
    val deleteState by postViewModel.clubPostDeleteState.collectAsState()
    var openDialog by remember { mutableStateOf(false) }
    val mainColor by mainViewModel.colorState.collectAsState()
    var menuDialog by remember { mutableStateOf(false) }
    var askToDeleteDialog by remember { mutableStateOf(false) }
    var deleteCompleteDialog by remember { mutableStateOf(false) }
    var deleteFailDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        YesOrNoDialog(content = "답글 작성을 취소하시겠습니까?", mainColor, onClickCancel = {
            openDialog = false
        }) {
            commentViewModel.isReply.value = false
        }
    }

    if (menuDialog) {
        PostManagementDialog({ menuDialog = false }) {
            askToDeleteDialog = true
        }

    }

    if (askToDeleteDialog) {
        YesOrNoDialog(content = "게시물을 삭제하시겠습니까?", mainColor, onClickCancel = {
            askToDeleteDialog = false
        }) {
            postViewModel.deletePost(pId)
        }
    }

    if (deleteCompleteDialog) {
        ConfirmDialog(content = "삭제가 완료되었습니다.", mainColor = mainColor) {
            navController.popBackStack()
            mainViewModel.beforeStack.value = "clubScreen"
            deleteCompleteDialog = false
        }
    }

    if (deleteFailDialog) {
        ConfirmDialog(content = "실패했습니다.\n다시 시도해 주세요.", mainColor = mainColor) {
            mainViewModel.beforeStack.value = "clubScreen"
            deleteFailDialog = false
        }
    }

    LaunchedEffect(deleteState) {
        if (deleteState == true) deleteCompleteDialog = true
        else if (deleteState == false) deleteFailDialog = true
    }


    LaunchedEffect(isWished) {
        wishViewModel.checkIsWishedPost(pId, 2)
    }

    LaunchedEffect(commentList) {
        postViewModel.getOneClubPost(pId)
        postViewModel.getClubPostingUser(pId)
        commentViewModel.getCommentListByPId(pId, 2)
        Log.d("미란 컴포지션", "여기서 일어났다. 디테일 페이지")
    }
    LaunchedEffect(replyList) {
        commentViewModel.getReplyListByCId(repliedCId, 2)
        commentViewModel.getReplyUser(repliedCId, 2)
    }

    LaunchedEffect(isReply) {
        if (beforeCId != 0) {
            commentViewModel.getReplyUser(beforeCId, 2)
            commentViewModel.getReplyListByCId(beforeCId, 2)
        }
    }

    if (beforeCId != 0) {
        LaunchedEffect(replyList[beforeCId]) {
            commentViewModel.getReplyUser(beforeCId, 2)
            commentViewModel.getReplyListByCId(beforeCId, 2)
        }
    }


    Scaffold(
        topBar = {
            if (post != null && user != null) {
                PostDetailAppBar(
                    commentViewModel,
                    wishViewModel,
                    mainViewModel,
                    mainColor,
                    pId,
                    2,
                    user,
                    navController
                ) {
                    menuDialog = true
                }
            }
        },
        bottomBar = {
            SendReply(
                isReply,
                postType = 2,
                pId = pId,
                value = content,
                commentViewModel = commentViewModel,
                mainColor = mainColor,
                onValueChange = { newValue ->
                    commentViewModel.content.value = newValue
                }) {
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
            Divider(thickness = 1.dp, color = colorResource(mainColor))
            when (postState) {
                is Resource.Error<ClubPostResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<ClubPostResponseModel> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(id = mainColor)
                        )
                    }
                }

                else -> {
                    if (user != null && post != null) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            PostUserInfo(user, post.date, navController)
                            ClubPostInfo(post)
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                            CommentWidget(
                                type = 2,
                                pId = pId,
                                mainColor,
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
fun ClubPostInfo(post: ClubPostResponseModel) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            post.title,
            style = boardDetailTitle,
            color = colorResource(
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
                post.content,
                style = boardDetailContent,
                color = colorResource(id = R.color.mainTextColor),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}