package com.example.haemo_kotlin.screen.main.board.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.comment.reply.ReplyResponseModel
import com.example.haemo_kotlin.model.retrofit.post.NoticeResponseModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.SendReply
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.util.convertDateWithoutHour
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.NoticeViewModel
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NoticeDetailScreen(
    nId: Int,
    noticeViewModel: NoticeViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val notice = noticeViewModel.noticeModel.collectAsState().value
    val postState = noticeViewModel.noticeModelState.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(notice, true) {
        launch {
            noticeViewModel.getNoticeById(nId)
        }
    }
    Scaffold(
        topBar = {
            SettingScreenAppBar(text = "공지사항", mainColor = mainColor, navController = navController)
        }
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
                is Resource.Error<NoticeResponseModel> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<NoticeResponseModel> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorResource(id = mainColor))
                    }
                }

                else -> {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
                        if (notice != null)
                            NoticeInfo(notice, mainColor)
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeInfo(notice: NoticeResponseModel, mainColor: Int) {
    val type = if (notice.category != "공지") "${notice.category} 공지" else "일반 공지"
    val convertedDate = convertDateWithoutHour(notice.date)
    Column() {
        Text(
            notice.title,
            color = colorResource(id = mainColor),
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            modifier = Modifier.padding(top = 30.dp, bottom = 15.dp)
        )
        Text(
            text = "$type | $convertedDate",
            modifier = Modifier.padding(bottom = 40.dp),
            fontSize = 12.sp,
            color = colorResource(id = R.color.noticeListScreenTextColor)
        )
        Text(
            notice.content,
            style = TextStyle(fontSize = 12.sp, lineHeight = 20.sp),
            color = colorResource(
                id = R.color.mainGreyColor
            )
        )
    }
}