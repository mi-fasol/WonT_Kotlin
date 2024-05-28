package com.example.haemo_kotlin.screen.main.board.register

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun PostRegisterScreen(
    viewModel: PostViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)
    val title = viewModel.title.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.postRegisterState.collectAsState().value

    var dialogOpen by remember { mutableStateOf(false) }
    var confirmDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<PostResponseModel> -> {
                confirmDialogOpen = true
            }

            is Resource.Error<PostResponseModel> -> {
                errorDialogOpen = true
            }

            else -> {
            }
        }
    }

    if (dialogOpen) {
        YesOrNoDialog(
            content = "등록하시겠습니까?",
            mainColor = mainColor,
            onClickCancel = { navController.popBackStack() }) {
            viewModel.registerPost()
            dialogOpen = false
            confirmDialogOpen = true
        }
    }
    if (confirmDialogOpen) {
        ConfirmDialog(
            content = "등록이 완료되었습니다.",
            mainColor = mainColor,
        ) {
            confirmDialogOpen = false
            navController.popBackStack()
        }
    }
    if (errorDialogOpen) {
        ConfirmDialog(
            content = "오류가 발생했습니다.\n다시 시도해 주세요.",
            mainColor = mainColor,
        ) {
            errorDialogOpen = false
        }
    }

    Box(
        modifier = Modifier.background(Color.White)
    ) {
        Column {
            PostRegisterAppBar(
                "모임 등록",
                mainColor = mainColor,
                navController
            )
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterRowField("모임", title, mainColor) {
                    viewModel.title.value = it
                }
                PostInfo(viewModel, mainColor)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(viewModel, null, null, 1, mainColor, navController) {
                    dialogOpen = true
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostInfo(viewModel: PostViewModel, mainColor: Int) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Column {
        Row(
            Modifier.width((screenWidth * 0.65).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropDownMenu(
                "0명",
                list = personList,
                modifier = Modifier
                    .weight(1f)
                    .width((screenWidth / 5).dp),
                mainColor
            ) {
                viewModel.person.value = personList.indexOf(it) + 1
            }
            DropDownMenu(
                "모임 카테고리",
                list = categoryList,
                modifier = Modifier
                    .weight(1.5f)
                    .width((screenWidth / 3.5).dp),
                mainColor
            ) {
                viewModel.category.value = it
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DropDownMenu(
                "2024년",
                list = yearList,
                modifier = Modifier
                    .width((screenWidth / 5.5).dp),
                mainColor
            ) {
                viewModel.deadlineYear.value = it
            }
            DropDownMenu(
                "1월",
                list = monthList,
                modifier = Modifier.width((screenWidth / 8).dp),
                mainColor
            ) {
                viewModel.deadlineMonth.value = it
            }

            DropDownMenu(
                "1일",
                list = dayList,
                modifier = Modifier.width((screenWidth / 8).dp),
                mainColor
            ) {
                viewModel.deadlineDay.value = it
            }

            DropDownMenu(
                "01시",
                list = hourList,
                modifier = Modifier.width((screenWidth / 8).dp),
                mainColor
            ) {
                viewModel.deadlineTime.value = it
            }
        }
    }
}