package com.example.haemo_kotlin.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.model.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainBottomNavigation
import com.example.haemo_kotlin.util.MyPageListAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.PostDetailAppBar
import com.example.haemo_kotlin.util.convertDate
import com.example.haemo_kotlin.viewModel.WishViewModel

@Composable
fun MyWishHotPlaceScreen(
    wishViewModel: WishViewModel,
    navController: NavController,
    uId: Int,
) {
    val post = wishViewModel.wishHotPlaceList.collectAsState().value
    val postState = wishViewModel.hotPlaceModelListState.collectAsState().value


    LaunchedEffect(post) {
        wishViewModel.getWishHotPlace(uId)
    }

    Scaffold(
        topBar = {
            MyPageListAppBar(navController)
        },
        bottomBar = {
            MainBottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 1.dp, color = colorResource(id = R.color.mainColor))
            when (postState) {
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
                    when (post.size) {
                        0 ->
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ErrorScreen("찜한 장소가 아직 없어요!")
                            }

                        else ->
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    "가고 싶은 모임",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(
                                        id = R.color.myBoardColor
                                    ),
                                    modifier = Modifier.padding(vertical = 15.dp)
                                )
                                Divider(thickness = 0.7.dp, color = Color(0xffbbbbbb))
                                MyWishHotPlaceList(post, uId, wishViewModel, navController)
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWishHotPlaceList(
    postList: List<HotPlaceResponsePostModel>, uId: Int, viewModel: WishViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 5.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        content = {
            items(postList.size) { idx ->
                MyWishHotPlaceItem(postList[idx], viewModel, navController)
            }
        }
    )
}

@Composable
fun MyWishHotPlaceItem(
    post: HotPlaceResponsePostModel,
    viewModel: WishViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val buttonClick = remember {
        mutableStateOf(false)
    }
    val buttonColor = if (buttonClick.value) R.color.white else R.color.mainColor
    Box(
        modifier = Modifier
            .height((screenHeight / 6).dp)
            .padding(top = 15.dp)
            .width((screenWidth / 3.5).dp)
            .clickable {
                //    navController.navigate(NavigationRoutes.HotPlaceDetailScreen)
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
        }
        Column(
            Modifier
                .padding(13.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = colorResource(id = buttonColor),
                modifier = Modifier
                    .size((screenWidth / 20).dp)
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .clickable {
                        buttonClick.value = !buttonClick.value
                    }
            )
            Text(
                post.title,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}