package com.example.haemo_kotlin.screen.main.board.list

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.WishButton
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HotPlaceScreen(
    postViewModel: HotPlacePostViewModel,
    wishViewModel: WishViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val postList = postViewModel.hotPlacePostList.collectAsState().value
    val popularPostList = postViewModel.popularHotPlace.collectAsState().value
    val postListState = postViewModel.hotPlacePostListState.collectAsState().value
    val context = LocalContext.current
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)

    LaunchedEffect(Unit) {
        postViewModel.getHotPlacePost()
        postViewModel.getPopularHotPlace()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("요즘 핫한 핫플레이스", mainColor, navController)
        },
    ) { innerPadding ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
                when (postListState) {
                    is Resource.Error<List<HotPlaceResponsePostModel>> -> {
                        ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                    }

                    is Resource.Loading<List<HotPlaceResponsePostModel>> -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            PopularPlace(
                                popularPostList, mainViewModel,
                                wishViewModel,
                                mainColor,
                                navController
                            )
                            HotPlaceBoard(
                                postList = postList,
                                viewModel = mainViewModel,
                                wishViewModel,
                                mainColor,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularPlace(
    postList: List<HotPlaceResponsePostModel>,
    viewModel: MainViewModel,
    wishViewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    when (postList.size) {
        0 -> {
            Box {}
        }

        else -> {
            val wishedPost by wishViewModel.wishHotPlaceList.collectAsState()
            LaunchedEffect(wishedPost) {
                wishViewModel.getWishHotPlace()
            }

            Column(
                Modifier.padding(top = 15.dp, bottom = 10.dp)
            ) {
                Text(
                    "현재, 가장 인기있는 핫플",
                    fontSize = 15.sp,
                    color = Color(0xff414141),
                    fontWeight = FontWeight.SemiBold
                )
                LazyRow(
                ) {
                    items(postList.size) { idx ->
                        PopularPlaceItem(
                            postList[idx],
                            viewModel,
                            wishViewModel,
                            mainColor,
                            navController
                        )
                        if (idx < postList.size - 1) {
                            Spacer(modifier = Modifier.width(15.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularPlaceItem(
    post: HotPlaceResponsePostModel,
    viewModel: MainViewModel,
    wishViewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    var isWished by remember { mutableStateOf(false) }

    LaunchedEffect(post) {
        isWished = wishViewModel.checkIsWishedPost(post.hpId, 3)
    }

    Row {
        Box(
            modifier = Modifier
                .height((screenHeight / 3.32).dp)
                .padding(top = 5.dp, bottom = 15.dp)
                .width((screenWidth / 2.15).dp)
                .clickable {
                    viewModel.beforeStack.value = "hotPlaceScreen"
                    navController.navigate(
                        NavigationRoutes.HotPlacePostDetailScreen.createRoute(
                            post.hpId
                        )
                    )
                }
                .background(Color.Unspecified, RoundedCornerShape(15.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Unspecified, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(15.dp)),
                    elevation = 0.dp,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black, RoundedCornerShape(15.dp))
                            .alpha(0.7f)
                    )
                    Column(
                        Modifier
                            .padding(13.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        WishButton(null, null, post, mainColor, 3, wishViewModel = wishViewModel)
                        Column(
                        ) {
                            Text(
                                post.title,
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                post.content,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HotPlaceBoard(
    postList: List<HotPlaceResponsePostModel>,
    viewModel: MainViewModel,
    wishViewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    Column {
        Text(
            "이런 장소는 어때요?", fontSize = 15.sp,
            color = Color(0xff414141),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 5.dp)
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(postList.size) { idx ->
                    HotPlaceBoardItem(
                        postList[idx],
                        viewModel,
                        wishViewModel,
                        mainColor,
                        navController
                    )
                }
            }
        )
    }
}

@Composable
fun HotPlaceBoardItem(
    post: HotPlaceResponsePostModel,
    viewModel: MainViewModel,
    wishViewModel: WishViewModel,
    mainColor: Int,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    var isWished by remember { mutableStateOf(false) }
    val wishPlaces = wishViewModel.wishHotPlaceList.collectAsState().value
    LaunchedEffect(Unit) {
        isWished = false
        wishViewModel.getWishHotPlace()
        isWished =

            wishPlaces.contains(post)
    }

    Box(
        modifier = Modifier
            .height((screenHeight / 5.8).dp)
            .padding(top = 15.dp)
            .width((screenWidth / 3.5).dp)
            .clickable {
                viewModel.beforeStack.value = "hotPlaceScreen"
                navController.navigate(NavigationRoutes.HotPlacePostDetailScreen.createRoute(post.hpId))
            },
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp)),
            elevation = 0.dp,
        ) {
            Image(
                painter = painterResource(id = R.drawable.dummy_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black, RoundedCornerShape(15.dp))
                    .alpha(0.7f)
            )
            Column(
                Modifier
                    .padding(13.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                WishButton(null, null, post, mainColor, 3, wishViewModel = wishViewModel)
                Text(
                    post.title,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}