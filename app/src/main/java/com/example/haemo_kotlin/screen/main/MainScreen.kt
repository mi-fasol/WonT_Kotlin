package com.example.haemo_kotlin.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.model.PostModel
import com.example.haemo_kotlin.util.BackArrowAppBar
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.viewModel.PostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(postViewModel: PostViewModel, navController: NavController) {
    val postList = postViewModel.postModelList.collectAsState().value

    LaunchedEffect(true) {
        postViewModel.getPost()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("친구 구하는 곳", navController)
        },
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp)) {
            Today24HoursBoard(postList, postViewModel)
        }
    }
}

@Composable
fun Today24HoursBoard(postList: List<PostModel>, viewModel: PostViewModel) {
    Column() {
        Text("공지 24시간", fontSize = 9.sp, color = Color(0xff393939))
        LazyRow(
        ) {
            items(postList.size) { idx ->
                TodayNotice(postList[idx], viewModel)
            }
        }
    }
}

@Composable
fun TodayNotice(post: PostModel, viewModel: PostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Box(
        modifier = Modifier
            .blur(30.dp)
    ) {
        Card(
            Modifier
                .padding(top = 5.dp, bottom = 15.dp, end = 10.dp)
                .height((screenHeight / 5.5).dp)
                .width((screenWidth / 3).dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, Color(0xff82C0EA))
        ) {
            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = post.title, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text("${post.person}명", fontSize = 12.sp, color = Color(0xff595959))
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            viewModel.convertDate(post.deadline),
                            fontSize = 12.sp,
                            color = Color(0xff595959),
                            modifier = Modifier.weight(8f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}
