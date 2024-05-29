package com.example.haemo_kotlin.screen.main.board.register

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.model.retrofit.post.NoticeResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.NoticeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun NoticeRegisterScreen(
    viewModel: NoticeViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val mainColor by mainViewModel.colorState.collectAsState()
    val title = viewModel.title.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.noticeRegisterState.collectAsState().value
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    var dialogOpen by remember { mutableStateOf(false) }
    var confirmDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<NoticeResponseModel> -> {
                confirmDialogOpen = true
            }

            is Resource.Error<NoticeResponseModel> -> {
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
            viewModel.registerNotice()
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
            SettingScreenAppBar(
                "공지사항 등록",
                mainColor = mainColor,
                navController
            )
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterRowField("제목", title, mainColor) {
                    viewModel.title.value = it
                }
                Spacer(modifier = Modifier.height(20.dp))
                DropDownMenu(
                    "안내",
                    list = noticeTypeList,
                    modifier = Modifier
                        .width((screenWidth / 5).dp),
                    mainColor
                ) {
                    viewModel.category.value = it
                }
                Spacer(modifier = Modifier.height(20.dp))
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                NoticeRegisterButton(viewModel, mainColor) {
                    dialogOpen = true
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoticeRegisterButton(
    viewModel: NoticeViewModel,
    mainColor: Int,
    onClicked: () -> Unit
) {
    val isValid by viewModel.isValid.collectAsState()

    Button(
        onClick = {
            onClicked()
        },
        enabled = isValid,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = mainColor),
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.White,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        Text(
            "등록하기",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
    }
}