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
import androidx.compose.runtime.getValue
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
import com.example.haemo_kotlin.model.retrofit.chat.ChatMessageModel
import com.example.haemo_kotlin.ui.theme.chatMessage
import com.example.haemo_kotlin.util.ChatRoomAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.chat.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    mainViewModel: MainViewModel,
    receiverId: Int,
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val uId = SharedPreferenceUtil(context).getInt("uId", 0)
    val receiver = chatViewModel.receiverInfo.collectAsState().value
    val chatMessage = chatViewModel.chatMessages.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()
    val chatId by chatViewModel.chatId.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            receiver?.let {
                ChatRoomAppBar(
                    it.nickname,
                    mainColor,
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
                uId,
                mainColor,
                chatViewModel
            )
            if (receiver != null) {
                SendMessage(
                    chatViewModel,
                    receiverId,
                    "${receiverId}+${uId}",
                    uId,
                    mainColor
                )
            }
        }
    }

    LaunchedEffect(Unit, key2 = chatId) {
        if (chatId.isNotBlank()) {
            chatViewModel.getChatRoomInfo(chatId, receiverId)
        } else {
            chatViewModel.findChatRoom(receiverId)
        }
    }
}

@Composable
fun ChatMessageField(
    message: List<ChatMessageModel>?,
    uId: Int,
    mainColor: Int,
    chatViewModel: ChatViewModel
) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        message?.let {
            items(it) { message ->
                MessageItem(message, uId, mainColor, chatViewModel)
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessageModel, uId: Int, mainColor: Int, chatViewModel: ChatViewModel) {
    val messageFromMe = message.from == uId

    if (messageFromMe) {
        MessageItemFromMe(message = message, mainColor, chatViewModel)
    } else {
        MessageItemFromOther(message = message, chatViewModel)
    }
}

@Composable
fun MessageItemFromMe(message: ChatMessageModel, mainColor: Int, chatViewModel: ChatViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = chatViewModel.formatDateTime(message.createdAt),
            color = colorResource(id = R.color.mainGreyColor),
            style = chatMessage,
            modifier = Modifier.padding(start = 7.dp)
        )
        Box(
            modifier = Modifier
                .background(
                    color = colorResource(id = mainColor).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                )
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = message.content,
                color = colorResource(id = R.color.mainTextColor),
                style = chatMessage
            )
        }
    }
}

@Composable
fun MessageItemFromOther(message: ChatMessageModel, chatViewModel: ChatViewModel) {
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
                    style = chatMessage,
                    color = colorResource(id = R.color.mainTextColor)
                )
            }
            Text(
                text = chatViewModel.formatDateTime(message.createdAt),
                color = colorResource(id = R.color.mainGreyColor),
                modifier = Modifier.padding(start = 7.dp),
                style = chatMessage
            )
        }
    }
}

@Composable
fun SendMessage(
    viewModel: ChatViewModel,
    receiverId: Int,
    chatId: String,
    userId: Int,
    mainColor: Int
) {
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
        Row {
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
                    .padding(10.dp),
                textStyle = chatMessage
            )
            FilledIconButton(
                onClick = {
                    if (sendMessage.value.isNotEmpty()) {
                        timestamp.longValue = System.currentTimeMillis()
                        val message =
                            ChatMessageModel(
                                sendMessage.value,
                                timestamp.longValue,
                                userId,
                                SharedPreferenceUtil(context).getString("nickname", "")!!,
                                false
                            )
                        viewModel.sendMessage(chatId, receiverId, message)
                        sendMessage.value = ""
                    }
                },
                shape = IconButtonDefaults.filledShape,
                colors = IconButtonColors(
                    containerColor = colorResource(id = mainColor),
                    contentColor = Color.White,
                    disabledContainerColor = colorResource(id = mainColor),
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