package com.example.haemo_kotlin.screen.main.board.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.retrofit.comment.reply.ReplyResponseModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.AttendUserDialog
import com.example.haemo_kotlin.util.CommentWidget
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.PostManagementDialog
import com.example.haemo_kotlin.util.PostUserInfo
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.YesOrNoDialog
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.AcceptState
import com.example.haemo_kotlin.viewModel.board.AcceptationViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MeetingPostDetailScreen(
    pId: Int,
    postViewModel: PostViewModel,
    commentViewModel: CommentViewModel,
    acceptationViewModel: AcceptationViewModel,
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val post by postViewModel.postModel.collectAsState()
    val user by postViewModel.user.collectAsState()
    val postState by postViewModel.postModelState.collectAsState()
    val accept by acceptationViewModel.attendeeModelList.collectAsState()
    val commentList by commentViewModel.commentList.collectAsState()
    val content by commentViewModel.content.collectAsState()
    val isReply by commentViewModel.isReply.collectAsState()
    val replyList by commentViewModel.replyList.collectAsState()
    val repliedCId by commentViewModel.commentId.collectAsState()
    val isWished by wishViewModel.isWished.collectAsState()
    val mainColor by mainViewModel.colorState.collectAsState()

    var replies by remember { mutableStateOf<List<ReplyResponseModel>>(emptyList()) }
    var replyUserList by remember { mutableStateOf<List<UserResponseModel>>(emptyList()) }

    var openDialog by remember {
        mutableStateOf(false)
    }
    val deleteState by postViewModel.postDeleteState.collectAsState()
    var askToDeleteDialog by remember { mutableStateOf(false) }
    var deleteCompleteDialog by remember { mutableStateOf(false) }
    var menuDialog by remember { mutableStateOf(false) }
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

    LaunchedEffect(accept) {
        acceptationViewModel.getAcceptationByPId(pId)
        acceptationViewModel.getAttendeeByPId(pId)
    }

    LaunchedEffect(deleteState) {
        if (deleteState == true) deleteCompleteDialog = true
        else if (deleteState == false) deleteFailDialog = true
    }

    LaunchedEffect(Unit, true) {
        launch {
            wishViewModel.checkIsWishedPost(pId, 1)
            postViewModel.getOnePost(pId)
            postViewModel.getPostingUser(pId)
            acceptationViewModel.getAcceptationByPId(pId)
            acceptationViewModel.getAttendeeByPId(pId)
            commentViewModel.getCommentListByPId(pId, 1)
            commentViewModel.getReplyListByCId(repliedCId, 1)
            commentViewModel.getReplyUser(repliedCId, 1)
        }
        Log.d("미란 라리루루루", replyList.toString())
        Log.d("미란 라라라참여", accept.toString())
    }

    LaunchedEffect(isWished) {
        wishViewModel.checkIsWishedPost(pId, 1)
        Log.d("미란링료료2", isWished.toString())
    }

    LaunchedEffect(commentList) {
        Log.d("미란 코멘트", "호출ㄹ")
        commentViewModel.getCommentListByPId(pId, 1)
    }

    LaunchedEffect(repliedCId) {
        commentViewModel.getReplyListByCId(repliedCId, 1)
        commentViewModel.getReplyUser(repliedCId, 1)
//        replies = commentViewModel.replyList.value
    }

    Scaffold(
        topBar = {
            if (post != null && user != null) {
                PostDetailAppBar(
                    commentViewModel,
                    wishViewModel,
                    mainViewModel,
                    mainColor,
                    post!!.pId,
                    1,
                    user!!,
                    navController
                ) {
                    menuDialog = true
                }
            }
        },
        bottomBar = {
            SendReply(
                isReply,
                postType = 1,
                pId = pId,
                value = content,
                commentViewModel = commentViewModel,
                mainColor = mainColor,
                onValueChange = { newValue ->
                    commentViewModel.content.value = newValue
                }) {
                commentViewModel.content.value = ""
            }
        },
//        modifier = Modifier.pointerInput(Unit) {
//            awaitEachGesture {
//                if (isReply) {
//                    openDialog = true
//                }
//            }
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(mainColor))
            when (postState) {
                is Resource.Error<PostResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<PostResponseModel> -> {
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
                            PostUserInfo(user!!, post!!.date, navController)
                            PostInfo(
                                post!!,
                                mainColor,
                                accept[pId],
                                user!!.nickname,
                                acceptationViewModel,
                                mainViewModel
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                            CommentWidget(
                                type = 1,
                                pId = pId,
                                commentViewModel = commentViewModel,
                                mainColor = mainColor,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
    if (openDialog) {
        YesOrNoDialog(content = "답글 작성을 취소하시겠습니까?", mainColor = mainColor, onClickCancel = {
            openDialog = false
        }) {
            commentViewModel.isReply.value = false
        }
    }
}

@Composable
fun PostInfo(
    post: PostResponseModel,
    mainColor: Int,
    accept: List<AcceptationResponseModel>?,
    nickname: String,
    acceptationViewModel: AcceptationViewModel,
    mainViewModel: MainViewModel
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp
    val acceptState by acceptationViewModel.myAcceptState.collectAsState()
    val deleteAcceptState by acceptationViewModel.acceptDeleteState.collectAsState()
    val registerState by acceptationViewModel.acceptRegisterState.collectAsState()
    val attendees by acceptationViewModel.attendeeList.collectAsState()
    val requestDialog = remember { mutableStateOf(false) }
    val deleteRequestDialog = remember { mutableStateOf(false) }
    val acceptUserDialog = remember { mutableStateOf(false) }
    val completeWorkDialog = remember { mutableStateOf(false) }
    val notMyPost = (nickname != mainViewModel.nickname || mainViewModel.role == "ADMIN")
    val allowedUser = accept?.filter { it.acceptation }?.size ?: 0

    LaunchedEffect(registerState, deleteAcceptState) {
        acceptationViewModel.getAcceptationByPId(post.pId)
        acceptationViewModel.getAttendeeByPId(post.pId)
    }

    val text = if (notMyPost) {
        when (acceptState) {
            AcceptState.NONE -> "참여하기"
            AcceptState.REQUEST -> "참여대기"
            else -> "참여완료"
        }
    } else "명단확인"

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
                "$allowedUser/${post.person}",
                color = colorResource(mainColor),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height((screenHeight / 26).dp)
                    .width((screenWidth / 5).dp)
                    .background(
                        colorResource(mainColor),
                        RoundedCornerShape(23.dp)
                    )
                    .clickable {
                        if (notMyPost) {
                            if (text == "참여하기") {
                                requestDialog.value = true
                            } else {
                                deleteRequestDialog.value = true
                            }
                        } else {
                            acceptUserDialog.value = true
                        }
                    },
            ) {
                Text(
                    text,
                    fontSize = 9.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (requestDialog.value) {
        YesOrNoDialog(content = "모임에 참여하시겠습니까?", mainColor = mainColor, onClickCancel = {
            requestDialog.value = false
        }) {
            acceptationViewModel.sendAcceptationRequest(post.pId)
            requestDialog.value = false
            completeWorkDialog.value = true
        }
    }

    if (deleteRequestDialog.value) {
        YesOrNoDialog(content = "참여를 취소하시겠습니까?", mainColor = mainColor, onClickCancel = {
            deleteRequestDialog.value = false
        }) {
            acceptationViewModel.deleteAcceptationRequest(post.pId)
            deleteRequestDialog.value = false
            completeWorkDialog.value = true
        }
    }

    if (completeWorkDialog.value) {
        ConfirmDialog(content = "완료되었습니다.", mainColor = mainColor) {
            completeWorkDialog.value = false
        }
    }

    if (acceptUserDialog.value) {
        if (accept!!.isNotEmpty()) {
            Log.d("미란 유저 리스트", attendees[post.pId]!!.toString())
            AttendUserDialog(
                attendList = accept,
                attendees = attendees[post.pId]!!,
                mainColor = mainColor,
                acceptationViewModel,
                onClickCancel = { acceptUserDialog.value = false })
        }
    }
}