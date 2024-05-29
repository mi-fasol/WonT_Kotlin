package com.example.haemo_kotlin.screen.main.board.list

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.NoticeResponseModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.NoticeViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoticeScreen(
    viewModel: NoticeViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val noticeList = viewModel.noticeList.collectAsState().value
    val noticeListState = viewModel.noticeListState.collectAsState().value
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(noticeList) {
        viewModel.getAllNotice()
    }

    Scaffold(
        topBar = {
            SettingScreenAppBar("공지사항", mainColor, navController)
        },
        backgroundColor = colorResource(id = R.color.settingScreenBackgroundColor)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when (noticeListState) {
                is Resource.Error<List<NoticeResponseModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<NoticeResponseModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    Column {
                        NoticeListField(
                            noticeList = noticeList,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeListField(
    noticeList: List<NoticeResponseModel>,
    navController: NavController
) {
    when (noticeList.size) {
        0 -> {
            ErrorScreen("등록된 글이 아직 없어요!")
        }

        else -> {
            LazyColumn {
                items(noticeList.size) { idx ->
                    NoticeListItem(
                        noticeList[idx],
                        navController
                    )
                }
            }
        }
    }
}

@Composable
fun NoticeListItem(
    notice: NoticeResponseModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val date = convertDate(notice.date)

    Box(
        modifier = Modifier.padding(top = 10.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .background(Color.White)
                .height((screenHeight / 10).dp)
                .clickable {
                    navController.navigate(NavigationRoutes.NoticeDetailScreen.createRoute(notice.nId))
                }
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(vertical = 13.dp, horizontal = 13.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "[${notice.category}] ${notice.title}",
                    fontSize = 13.sp,
                    color = colorResource(
                        id = R.color.mainGreyColor
                    )
                )
                Text(
                    text = date,
                    fontSize = 11.sp,
                    color = colorResource(id = R.color.noticeListScreenTextColor)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorResource(
                    id = R.color.settingScreenTitleTextColor
                )
            )
        }
    }
}