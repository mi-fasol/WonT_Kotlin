package com.example.haemo_kotlin.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.retrofit.comment.reply.ReplyResponseModel
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.ui.theme.commentContent
import com.example.haemo_kotlin.ui.theme.commentInfo
import com.example.haemo_kotlin.ui.theme.commentUserNickname
import com.example.haemo_kotlin.ui.theme.postUserInfo
import com.example.haemo_kotlin.ui.theme.postUserNickname
import com.example.haemo_kotlin.ui.theme.replyButtonText
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.launch

@Composable
fun ErrorScreen(text: String) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = R.drawable.error_image),
                contentDescription = null,
                modifier = Modifier.size((screenWidth / 4).dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = text,
                color = Color(0xffc2c2c2),
                fontSize = 12.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterInfo(type: String, mainColor: Int, value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            Text(type, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                visualTransformation = if (type != "P/W") VisualTransformation.None
                else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = mainColor),
                    unfocusedBorderColor = Color.LightGray,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp)
            )
        }
    }
}

@Composable
fun PostUserInfo(user: UserResponseModel, date: String, navController: NavController) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp

    var bottomSheetOpen by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    bottomSheetOpen = true
                },
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = userMyPageImageList[user.userImage]),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size((screenHeight / 18).dp)
                )
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = user.nickname,
                    color = Color(0xff3f3f3f),
                    style = postUserNickname
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${user.major} / ", style = postUserInfo, color = Color(0xff3f3f3f))
                    val iconColor =
                        if (user.gender == "남자") colorResource(id = R.color.mainColor) else colorResource(
                            id = R.color.pinkMainColor
                        )
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(8.5.dp)
                    )
                    Text(text = " / $date", style = postUserInfo, color = Color(0xff3f3f3f))
                }
            }
        }
        if (bottomSheetOpen) {
            UserBottomSheet(user = user, navController = navController) {
                bottomSheetOpen = false
            }
        }
    }
}

@Composable
fun SendReply(
    isReply: Boolean,
    postType: Int,
    pId: Int,
    mainColor: Int,
    commentViewModel: CommentViewModel,
    value: String,
    onValueChange: (String) -> Unit,
    onClickedEvent: () -> Unit
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val cId = commentViewModel.commentId.collectAsState().value

    LaunchedEffect(Unit) {
        launch {
            commentViewModel.getCommentListByPId(pId, postType)
            commentViewModel.getReplyListByCId(cId, postType)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .height((screenWidth / 9).dp)
                    .background(Color(0xffededed), RoundedCornerShape(25.dp))
                    .padding(10.dp)
            )
            FilledIconButton(
                onClick = {
                    SharedPreferenceUtil(context).setString("nickname", "뜽미니에요")
                    if (!isReply) {
                        commentViewModel.registerComment(value, pId, postType)
                    } else {
                        commentViewModel.registerReply(value, cId, postType)
                    }
                    onClickedEvent()
                },
                shape = IconButtonDefaults.filledShape,
                colors = IconButtonColors(
                    containerColor = colorResource(mainColor),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(mainColor),
                    disabledContentColor = Color.White
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_comment_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((screenWidth / 20).dp)
                )
            }
        }
    }
}

@Composable
fun CommentWidget(
    type: Int,
    pId: Int,
    mainColor: Int,
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val commentList by commentViewModel.commentList.collectAsState()
    val userList by commentViewModel.userList.collectAsState()

    LaunchedEffect(commentList) {
        commentViewModel.getCommentListByPId(pId, type)
        commentViewModel.getCommentUser(pId, type)
        Log.d("미란 댓글", commentList.toString())
    }

    Column(Modifier.padding(horizontal = 20.dp)) {
        Row(modifier = Modifier.padding(vertical = 15.dp)) {
            Text(
                "댓글",
                style = commentInfo,
                color = Color(0xff040404),
            )
            Spacer(Modifier.width(5.dp))
            Text(
                commentList.size.toString(),
                style = commentInfo,
                color = colorResource(
                    id = mainColor
                )
            )
        }
        if (commentList.isNotEmpty()) {
            commentList.forEachIndexed { index, comment ->
                LaunchedEffect(commentList) {
                    commentViewModel.getCommentUser(pId, type)
                }
                if (userList.size == commentList.size) {
                    CommentWidgetItem(
                        comment,
                        userList[index],
                        type,
                        mainColor,
                        commentViewModel,
                        navController
                    )
                }
            }
        }
    }
}

@Composable
fun ReplyWidget(
    cId: Int,
    type: Int,
    replyList: List<ReplyResponseModel>?,
    users: List<UserResponseModel>?,
    viewModel: CommentViewModel,
    navController: NavController
) {
    val replies by viewModel.replyList.collectAsState()
    val replyUsers by viewModel.replyUserList.collectAsState()
    val replyState by viewModel.replyRegisterState.collectAsState()

    LaunchedEffect(replyState, replyUsers) {
        viewModel.getReplyListByCId(cId, type)
        viewModel.getReplyUser(cId, type)
        Log.d("미란 대댓글", "실행됨")
    }
    LaunchedEffect(replies[cId]) {
        viewModel.getReplyUser(cId, type)
        viewModel.getReplyListByCId(cId, type)
        Log.d("미란 대댓글", "리플라이 리스트 바뀜")
    }

    Column {
        replyList?.forEachIndexed { index, reply ->
            LaunchedEffect(replies[cId]) {
                viewModel.getReplyListByCId(cId, type)
                viewModel.getReplyUser(cId, type)
                Log.d("미란 대댓글", "리플라이 리스트가 바뀌어서 실행이 됐어요")
            }
            LaunchedEffect(replyState) {
                viewModel.getReplyListByCId(cId, type)
                viewModel.getReplyUser(cId, type)
                Log.d("미란 대댓글", "리플라이 스테이트가 바뀌어서 실행이 됐어요")
            }
            if (users != null) {
                if (users.size == replyList.size) {
                    users.getOrNull(index)
                        ?.let { ReplyWidgetItem(reply, it, navController) }
                } else {
                    LaunchedEffect(replies[cId]) {
                        viewModel.getReplyListByCId(cId, type)
                        viewModel.getReplyUser(cId, type)
                        Log.d("미란 대댓글", "리플라이 리스트가 바뀌어서 실행이 됐어요")
                    }
                    LaunchedEffect(replyState) {
                        viewModel.getReplyListByCId(cId, type)
                        viewModel.getReplyUser(cId, type)
                        Log.d("미란 대댓글", "리플라이 스테이트가 바뀌어서 실행이 됐어요")
                    }
                }
            }
        }
    }
}


@Composable
fun CommentWidgetItem(
    comment: CommentResponseModel,
    user: UserResponseModel?,
    type: Int,
    mainColor: Int,
    viewModel: CommentViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp

    val replyList by viewModel.replyList.collectAsState()
    val replies = replyList[comment.cId]

    val userList by viewModel.replyUserList.collectAsState()
    val replyUsers = userList[comment.cId]
    var bottomSheetOpen by remember { mutableStateOf(false) }
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true, key2 = Unit) {
        launch {
            viewModel.getReplyListByCId(comment.cId, type)
            viewModel.getReplyUser(comment.cId, type)
        }
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .fillMaxHeight(),
        ) {
            IconButton(
                onClick = {
                    bottomSheetOpen = true
                },
                modifier = Modifier.padding(end = 5.dp)
            ) {
                if (user != null) {
                    Icon(
                        painter = painterResource(id = userMyPageImageList[user.userImage]),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size((screenHeight / 16).dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color.Black, CircleShape)
                            .size((screenHeight / 16).dp)
                    ) {}
                }
            }
            Column(
                Modifier.padding(vertical = 5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (user != null) comment.nickname else "탈퇴한 사용자입니다.",
                        style = commentUserNickname,
                        color = colorResource(id = R.color.mainTextColor),
                        modifier = Modifier
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                colorResource(id = R.color.meetingDetailButtonColor),
                                RoundedCornerShape(15.dp)
                            )
                            .clickable {
                                dialogOpen = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "답글",
                            style = replyButtonText,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 20.dp),
                            color = colorResource(id = R.color.meetingDetailButtonTextColor)
                        )
                    }
                }
                Text(
                    comment.content,
                    style = commentContent,
                    color = colorResource(id = R.color.mainTextColor),
                    maxLines = Int.MAX_VALUE
                )
            }
        }

        if (replies != null) {
            if (replies.isNotEmpty()) {
                LaunchedEffect(replies) {
                    viewModel.getReplyListByCId(comment.cId, type)
                    viewModel.getReplyUser(comment.cId, type)
                }
                ReplyWidget(
                    cId = comment.cId,
                    type = type,
                    replyList = replies,
                    users = replyUsers,
                    viewModel = viewModel,
                    navController = navController
                )
//                replies.forEachIndexed { index, reply ->
//                    if (replyUsers != null) {
//                        replyUsers.getOrNull(index)
//                            ?.let { ReplyWidgetItem(reply, it, navController) }
//                    }
//                }
            }
        }

//        if (replies.isNotEmpty()) {
//            replies.forEachIndexed { index, reply ->
//                if (replyUserList.isNotEmpty()) {
//                    replyUserList.getOrNull(index)
//                        ?.let { ReplyWidgetItem(reply, it, navController) }
//                }
//            }
//        }
    }

    if (bottomSheetOpen) {
        if (user != null)
            UserBottomSheet(user = user, navController = navController) {
                bottomSheetOpen = false
            }
    }

    if (dialogOpen) {
        YesOrNoDialog(content = "답글을 작성하시겠습니까?", mainColor, onClickCancel = {
            dialogOpen = false
        }) {
            viewModel.isReply.value = true
            viewModel.commentId.value = comment.cId
            viewModel.postType.value = type
            dialogOpen = false
        }
    }
}

@Composable
fun ReplyWidgetItem(
    reply: ReplyResponseModel,
    user: UserResponseModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp

    var bottomSheetOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 20.dp)
                .fillMaxHeight(),
        ) {
            Icon(
                painter = painterResource(R.drawable.reply_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size((screenHeight / 22).dp)
                    .padding(end = 10.dp)
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = reply.nickname,
                        style = commentUserNickname,
                        color = colorResource(id = R.color.mainTextColor),
                        modifier = Modifier
                    )
                }
                Text(
                    reply.content,
                    style = commentContent,
                    color = colorResource(id = R.color.mainTextColor),
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

@Composable
fun WishButton(
    post: PostResponseModel?,
    clubPost: ClubPostResponseModel?,
    hotPlacePost: HotPlaceResponsePostModel?,
    mainColor: Int,
    type: Int,
    wishViewModel: WishViewModel
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val icon =
        if (type == 3) painterResource(id = R.drawable.heart_icon) else painterResource(id = R.drawable.wish_meeting_icon)

    var isWished by remember { mutableStateOf(false) }
    val wishes = wishViewModel.wishMeetingList.collectAsState().value
    val wishClubs = wishViewModel.wishClubList.collectAsState().value
    val wishPlaces = wishViewModel.wishHotPlaceList.collectAsState().value
    val pId = when (type) {
        1 -> post!!.pId
        2 -> clubPost!!.pId
        else -> hotPlacePost!!.hpId
    }

    val iconColor =
        if (isWished) colorResource(mainColor) else colorResource(id = R.color.postRegisterTextColor)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, key2 = true) {
        isWished = false
        isWished = when (type) {
            1 -> {
                wishViewModel.getWishMeeting()
                wishes.contains(post)
            }

            2 -> {
                wishViewModel.getWishClub()
                wishClubs.contains(clubPost)
            }

            else -> {
                wishViewModel.getWishHotPlace()
                wishPlaces.contains(hotPlacePost)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size((screenWidth / 20).dp)
                .fillMaxWidth()
                .clickable {
                    coroutineScope.launch {
                        if (!isWished) {
                            wishViewModel.addWishList(pId, type)
                        } else {
                            wishViewModel.deleteWishList(pId, type)
                        }
                        isWished = !isWished
                    }
                }
        )
    }
}