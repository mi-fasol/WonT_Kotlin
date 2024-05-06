@file:Suppress("DEPRECATION")

package com.example.haemo_kotlin.screen.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FixedThreshold
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.ThresholdConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.example.haemo_kotlin.util.BackArrowAppBar
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.userMyPageImageList
import com.example.haemo_kotlin.viewModel.chat.ChatListViewModel
import kotlin.math.roundToInt

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

    LaunchedEffect(Unit) {
        viewModel.getChatList()
    }

    LazyColumn {
        items(chatList.size) { idx ->
            val lastMessage = viewModel.checkLastMessage(chatList[idx])
            val receiver = chatMap[chatList[idx].id]
            val swipeableState = rememberSwipeableState(initialValue = 0)
            val sizePx = with(LocalDensity.current) { ((screenWidth / 8).dp).toPx() }
            val anchors = mapOf(0f to 0, -sizePx to 1)

            if (receiver == null) {
                ErrorScreen(text = "잠시 후 다시 시도해 주세요.")
            } else {
                Column {
                    Box(
                        Modifier
                            .swipeable(
                                state = swipeableState,
                                orientation = Orientation.Horizontal,
                                anchors = anchors,
                                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                                velocityThreshold = 1000.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .background(Color.Red)
                                .fillMaxHeight()
                        ) {
                            Icon(
                                Icons.Default.Delete, contentDescription = null, tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        swipeableState.offset.value.roundToInt(),
                                        0
                                    )
                                }
                                .padding(top = 10.dp, start = 15.dp, bottom = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
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
                        }
                    }
                }
                Divider()
            }
        }
    }
}