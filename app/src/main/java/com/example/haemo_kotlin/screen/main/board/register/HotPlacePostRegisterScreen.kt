@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.example.haemo_kotlin.screen.main.board.register

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.model.retrofit.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ContentEnterField
import com.example.haemo_kotlin.util.PostRegisterAppBar
import com.example.haemo_kotlin.util.PostRegisterButton
import com.example.haemo_kotlin.util.TextEnterRowField
import com.example.haemo_kotlin.util.YesOrNoDialog
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun HotPlacePostRegisterScreen(
    viewModel: HotPlacePostViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {

    val title = viewModel.title.collectAsState().value
    val description = viewModel.description.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.hotPlacePostRegisterState.collectAsState().value
    var dialogOpen by remember { mutableStateOf(false) }
    var confirmDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<HotPlaceResponsePostModel> -> {
                confirmDialogOpen = true
            }

            is Resource.Error<HotPlaceResponsePostModel> -> {
                errorDialogOpen = true
            }

            else -> {
            }
        }
    }

    if (dialogOpen) {
        YesOrNoDialog(
            content = "등록하시겠습니까?",
            mainColor,
            onClickCancel = { navController.popBackStack() }) {
            viewModel.registerPost()
            dialogOpen = false
            confirmDialogOpen = true
        }
    }
    if (confirmDialogOpen) {
        ConfirmDialog(content = "등록이 완료되었습니다.", mainColor) {
            confirmDialogOpen = false
            navController.popBackStack()
        }
    }
    if (errorDialogOpen) {
        ConfirmDialog(content = "오류가 발생했습니다.\n다시 시도해 주세요.", mainColor) {
            errorDialogOpen = false
        }
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            PostRegisterAppBar("핫플 등록", mainColor, navController)
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterRowField("장소", title, mainColor) {
                    viewModel.title.value = it
                }
                TextEnterRowField("설명", description, mainColor) {
                    viewModel.description.value = it
                }
                HotPlacePostInfo(viewModel)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(null, null, viewModel, 3, mainColor, navController) {
                    dialogOpen = true
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HotPlacePostInfo(viewModel: HotPlacePostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    val images = viewModel.image.collectAsState().value

    LaunchedEffect(images) {

    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(bottom = 30.dp)
    ) {
        ImagePickerBox(viewModel = null, viewModel, Modifier, 3)
        LazyRow(
            Modifier.padding(start = 10.dp)
        ) {
            items(images.size) { idx ->
                Row(
                    Modifier
                        .size((screenWidth / 5).dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(images[idx]),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}