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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.model.post.HotPlaceResponsePostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun HotPlacePostRegisterScreen(viewModel: HotPlacePostViewModel, navController: NavController) {

    val title = viewModel.title.collectAsState().value
    val description = viewModel.description.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.hotPlacePostRegisterState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<HotPlaceResponsePostModel> -> {
                navController.popBackStack()
            }

            else -> {

            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            PostRegisterAppBar("핫플 등록", navController)
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterField("장소", title) {
                    viewModel.title.value = it
                }
                TextEnterField("설명", description) {
                    viewModel.description.value = it
                }
                HotPlacePostInfo(viewModel)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(null, null, viewModel, 3, navController) {
                    viewModel.registerPost(context)
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