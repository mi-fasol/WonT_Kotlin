package com.example.haemo_kotlin.screen.main.board.list

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ErrorScreen
import com.example.haemo_kotlin.util.MainPageAppBar
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClubScreen(postViewModel: ClubPostViewModel, navController: NavController) {
    val postList: List<ClubPostResponseModel> = postViewModel.clubPostList.collectAsState().value
    var searchText by remember { mutableStateOf("") }
    var filteredPosts by remember { mutableStateOf(postList) }
    val postListState = postViewModel.clubPostListState.collectAsState().value
    val list = if (searchText.isNotBlank()) filteredPosts else postList

    LaunchedEffect(Unit) {
        postViewModel.getClubPostList()
    }

    Scaffold(
        topBar = {
            MainPageAppBar("소모임/동아리 게시판", navController)
        },
//        bottomBar = {
//            MainBottomNavigation(navController = navController)
//        },
//        floatingActionButton = { PostRegisterFloatingButton(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding() + 10.dp
                )
        ) {
            Divider(thickness = 0.5.dp, color = Color(0xffbbbbbb))
            when (postListState) {
                is Resource.Error<List<ClubPostResponseModel>> -> {
                    ErrorScreen("오류가 발생했습니다.\n잠시 후 다시 시도해 주세요.")
                }

                is Resource.Loading<List<ClubPostResponseModel>> -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SearchBarWidget(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                filteredPosts = postList.filter { post ->
//                        post.containsHangulSearch(searchText)
                                    post.title.contains(it)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ClubBoard(postList = list, viewModel = postViewModel, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBarWidget(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Box(
        Modifier
            .padding(top = 20.dp)
            .background(Color(0xffededed), RoundedCornerShape(23.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .width((screenWidth / 0.7).dp)
                    .padding(16.dp)
                    .weight(5f),
                singleLine = true,
                textStyle = TextStyle(color = Color.Black) // Text color
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(colorResource(id = R.color.mainColor), CircleShape)
                    .size((screenWidth / 15).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null,
                    modifier = Modifier.size((screenWidth / 32).dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClubBoard(
    postList: List<ClubPostResponseModel>,
    viewModel: ClubPostViewModel,
    navController: NavController
) {
    LazyColumn(
    ) {
        items(postList.size) { idx ->
            ClubBoardItem(postList[idx], viewModel, navController)
            Divider()
        }
    }
}

@Composable
fun ClubBoardItem(
    post: ClubPostResponseModel,
    viewModel: ClubPostViewModel,
    navController: NavController
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    Box(
        modifier = Modifier
            .height((screenHeight / 7).dp)
            .padding(vertical = 18.dp, horizontal = 3.dp)
            .clickable {
                navController.navigate(NavigationRoutes.ClubPostDetailScreen.createRoute(post.pId))
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size((screenWidth / 6).dp)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(id = R.color.mainColor), CircleShape)
                )
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(3.5f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    Text(
                        text = post.title,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(id = R.color.mainColor)
                    )
                    Text(
                        text = post.title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff353535)
                    )
                }
                Column() {
                    Text(
                        post.description, fontSize = 10.sp,
                        color = Color(0xff414141)
                    )
                    LazyRow() {
                        items(2) {
                            Row() {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .height(10.dp)
                                        .background(
                                            Color(0xffededed),
                                            RoundedCornerShape(15.dp)
                                        )
                                        .padding(horizontal = 10.dp),
                                ) {
                                    Text("#검도남", fontSize = 8.5.sp, color = Color(0xff717171))
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
