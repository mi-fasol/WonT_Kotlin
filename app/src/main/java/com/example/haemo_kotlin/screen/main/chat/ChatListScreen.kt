package com.example.haemo_kotlin.screen.main.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.example.haemo_kotlin.util.BackArrowAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.userMyPageImageList
import com.example.haemo_kotlin.viewModel.UserViewModel
import com.example.haemo_kotlin.viewModel.chat.ChatListViewModel

@Composable
fun ChatListScreen(
    chatListViewModel: ChatListViewModel,
    userViewModel: UserViewModel,
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
        ) {
            ChatList(chatList, chatListViewModel, userViewModel, navController)
        }
    }

//    LaunchedEffect(chatList){
//        chatListViewModel.getChatList()
//    }
}

@Composable
fun ChatList(
    chatList: List<FireBaseChatModel>,
    viewModel: ChatListViewModel,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val nickname = SharedPreferenceUtil(context).getString("nickname", "")
    val receiverList = viewModel.receiverList.collectAsState().value
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    Log.d("미란 ChatListScreen", receiverList.toString())

    LazyColumn {
        items(chatList.size) { idx ->
            val lastMessage = viewModel.checkLastMessage(chatList[idx])
            val otherPersonNickname = viewModel.getNickname(chatList[idx], nickname!!)
            val receiverId = receiverList[idx]!!.uId
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 20.dp)
                    .clickable {
                        navController.navigate(NavigationRoutes.ChatScreen.createRoute(receiverId))
                    }
            ) {
                Image(
                    painterResource(id = userMyPageImageList[receiverList[idx]!!.userImage]),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 7).dp)
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(otherPersonNickname)
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

