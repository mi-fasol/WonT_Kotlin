package com.example.haemo_kotlin.screen.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.example.haemo_kotlin.model.chat.ChatMessageModel
import com.example.haemo_kotlin.util.ChatRoomAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.chat.ChatViewModel

@Composable
fun ChatScreen(chatViewModel: ChatViewModel,  receiverId: Int, navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)
    val receiver = chatViewModel.receiverInfo.collectAsState().value
    val chatMessage = chatViewModel.chatMessages.collectAsState().value
    val chatData = chatViewModel.fireBaseChatModel.collectAsState().value

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            receiver?.let {
                ChatRoomAppBar(
                    it.nickname,
                    navController
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            ChatMessageField(
                chatMessage,
                chatData,
                uId
            )
            SendMessage(chatViewModel, receiverId, "${receiverId}+${uId}", uId)
        }
    }

    LaunchedEffect(Unit) {
        chatViewModel.getChatRoomInfo("${receiverId}+${uId}", receiverId)
    }

}

@Composable
fun ChatMessageField(
    message: List<ChatMessageModel>?,
    fireBaseChatModel: FireBaseChatModel?,
    uId: Int
) {
    val sender: Int? = fireBaseChatModel?.sender?.id

    val nickname = if (sender == uId) {
        fireBaseChatModel.receiver.nickname
    } else {
        fireBaseChatModel?.sender?.nickname
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        message?.let {
            items(it) { message ->
                nickname?.let { nickname ->
                    MessageItem(message, uId, nickname)
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessageModel, uId: Int, nickname: String) {
    val messageFromMe = message.from == uId

    if (messageFromMe) {
        MessageItemFromMe(message = message)
    } else {
        MessageItemFromOther(message = message)
    }
}

@Composable
fun MessageItemFromMe(message: ChatMessageModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "방금",
            color = Color.Gray,
            modifier = Modifier.padding(start = 7.dp),
            fontSize = 13.sp
        )
        Box(
            modifier = Modifier
                .background(
                    color = colorResource(id = R.color.chatFromMeColor),
                    shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                )
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = message.content,
                color = colorResource(id = R.color.mainTextColor)
            )
        }
    }
}

@Composable
fun MessageItemFromOther(message: ChatMessageModel) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.chatFromOtherColor),
                        shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                    )
                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = message.content,
                    color = colorResource(id = R.color.mainTextColor)
                )
            }
            Text(
                text = "방금",
                color = Color.Gray,
                modifier = Modifier.padding(start = 7.dp),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun SendMessage(viewModel: ChatViewModel, receiverId: Int, chatId: String, userId: Int) {
    val sendMessage = remember {
        mutableStateOf("")
    }
    val timestamp = remember {
        mutableLongStateOf(0)
    }
    val context = LocalContext.current

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
        ) {
            BasicTextField(
                value = sendMessage.value,
                onValueChange = {
                    sendMessage.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
                    .height((screenWidth / 9).dp)
                    .background(Color(0xffededed), RoundedCornerShape(25.dp))
                    .padding(10.dp)
            )
            FilledIconButton(
                onClick = {
                    if (sendMessage.value.isNotEmpty()) {
                        timestamp.longValue = System.currentTimeMillis()
                        val message =
                            ChatMessageModel(sendMessage.value, false, timestamp.longValue, userId)
                        viewModel.sendMessage(chatId, receiverId, message)
                        sendMessage.value = ""
                    }
                },
                shape = IconButtonDefaults.filledShape,
                colors = IconButtonColors(
                    containerColor = colorResource(id = R.color.mainColor),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = R.color.mainColor),
                    disabledContentColor = Color.White
                ),
            ) {
                androidx.compose.material.Icon(
                    painter = painterResource(id = R.drawable.send_comment_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((screenWidth / 20).dp)
                )
            }
        }
    }
}