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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.comment.reply.ReplyResponseModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
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
fun EnterInfo(type: String, value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            androidx.compose.material3.Text(type, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xff82C0EA),
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
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                interactionSource = remember { MutableInteractionSource() },
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
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = user.nickname,
                    color = Color(0xff3f3f3f),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.5.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${user.major} / ", fontSize = 8.5.sp, color = Color(0xff3f3f3f))
                    val iconColor =
                        if (user.gender == "남자") Color(0xff82c0e2) else Color(0xffFF9B9B)
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(8.5.dp)
                    )
                    Text(text = " / $date", fontSize = 8.5.sp, color = Color(0xff3f3f3f))
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
    commentViewModel: CommentViewModel,
    value: String,
    onValueChange: (String) -> Unit,
    onClickedEvent: () -> Unit
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val cId = commentViewModel.commentId.collectAsState().value

    LaunchedEffect(Unit){
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
                    containerColor = colorResource(id = R.color.mainColor),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = R.color.mainColor),
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
    commentViewModel: CommentViewModel,
    navController: NavController
) {
    val commentList = commentViewModel.commentList.collectAsState().value
    val userList = commentViewModel.userList.collectAsState().value
    LaunchedEffect(commentList) {
        commentViewModel.getCommentListByPId(pId, type)
        commentViewModel.getCommentUser(pId, type)
    }
    Column(Modifier.padding(horizontal = 20.dp)) {
        Row(modifier = Modifier.padding(vertical = 15.dp)) {
            androidx.compose.material3.Text(
                "댓글",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff040404),
            )
            Spacer(Modifier.width(5.dp))
            androidx.compose.material3.Text(
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
                userList.getOrNull(index)
                    ?.let { CommentWidgetItem(comment, it, type, commentViewModel, navController) }
            }
        }
    }
}

@Composable
fun CommentWidgetItem(
    comment: CommentResponseModel,
    user: UserResponseModel,
    type: Int,
    viewModel: CommentViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    val replyList by viewModel.replyList.collectAsState()
    val replys = replyList[comment.cId]

    val userList by viewModel.replyUserList.collectAsState()
    val replyUsers = userList[comment.cId]

    var bottomSheetOpen by remember { mutableStateOf(false) }
    var dialogOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true, key2 = Unit) {
//        replies = emptyList()
        launch {
            viewModel.getReplyListByCId(comment.cId, type)
            viewModel.getReplyUser(comment.cId, type)
        }
//        if (viewModel.replyList.value[comment.cId] != null) {
//            replies = viewModel.replyList.value[comment.cId]!!
//        }
//        if (viewModel.replyUserList.value[comment.cId] != null) {
//            replyUserList = viewModel.replyUserList.value[comment.cId]!!
//        }
//        Log.d("미란 라리루루루 Component", replies.toString())
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
                }
            ) {
                Icon(
                    painter = painterResource(id = userMyPageImageList[user.userImage]),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size((screenHeight / 18).dp)
                )
            }
            Column(
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.Text(
                        text = comment.nickname,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
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
                            fontSize = 10.sp,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 20.dp),
                            color = colorResource(id = R.color.meetingDetailButtonTextColor)
                        )
                    }
                }
                androidx.compose.material3.Text(
                    comment.content, fontSize = 12.5.sp,
                    color = colorResource(id = R.color.mainTextColor),
                    maxLines = Int.MAX_VALUE
                )
            }
        }

        if (replys != null) {
            if (replys.isNotEmpty()) {
                replys.forEachIndexed { index, reply ->
                    if (replyUsers != null) {
                        replyUsers.getOrNull(index)
                            ?.let { ReplyWidgetItem(reply, it, navController) }
                    }
                }
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
        UserBottomSheet(user = user, navController = navController) {
            bottomSheetOpen = false
        }
    }

    if (dialogOpen) {
        YesOrNoDialog(content = "답글을 작성하시겠습니까?", onClickCancel = {
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
            Column(
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.material3.Text(
                        text = reply.nickname,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.mainTextColor),
                        modifier = Modifier
                    )
                }
                androidx.compose.material3.Text(
                    reply.content, fontSize = 12.5.sp,
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
        if (isWished) colorResource(id = R.color.mainColor) else colorResource(id = R.color.postRegisterTextColor)

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