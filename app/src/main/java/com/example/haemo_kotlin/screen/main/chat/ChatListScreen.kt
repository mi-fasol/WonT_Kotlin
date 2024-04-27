package com.example.haemo_kotlin.screen.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.example.haemo_kotlin.util.BackArrowAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.userMyPageImageList
import com.example.haemo_kotlin.viewModel.chat.ChatListViewModel

@Composable
fun ChatListScreen(
    chatListViewModel: ChatListViewModel,
    navController: NavController
) {
    val chatList = chatListViewModel.fireBaseChatModel.collectAsState().value
    Scaffold(
        topBar = {
            BackArrowAppBar(appBarText = "채팅 목록", navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White)
        ) {
            Divider(color = colorResource(id = R.color.mainColor))
            ChatList(chatList, chatListViewModel, navController)
        }
    }

    LaunchedEffect(true) {
        chatListViewModel.getChatList()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatList(
    chatList: List<FireBaseChatModel>,
    viewModel: ChatListViewModel,
    navController: NavController
) {
    val chatMap = viewModel.chatList.collectAsState().value
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    LazyColumn {
        items(chatList.size) { idx ->
            val lastMessage = viewModel.checkLastMessage(chatList[idx])
            val receiver = chatMap[chatList[idx].id]!!
            SwipeToDismiss(
                state = DismissState(
                    DismissValue.Default,
                ),
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(Color.Red),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                        .clickable {
                            navController.navigate(
                                NavigationRoutes.ChatScreen.createRoute(
                                    receiver.uId
                                )
                            )
                        }
                ) {
                    Image(
                        painterResource(id = userMyPageImageList[receiver.userImage]),
                        contentDescription = null,
                        modifier = Modifier.size((screenWidth / 7).dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(receiver.nickname)
                        Text(
                            text = lastMessage!!.content,
                            color = Color.DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
                Divider()
            }
        }
    }
}

