package com.example.haemo_kotlin.screen.main.board.register

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.post.ClubPostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ContentEnterField
import com.example.haemo_kotlin.util.DropDownMenu
import com.example.haemo_kotlin.util.PostRegisterAppBar
import com.example.haemo_kotlin.util.PostRegisterButton
import com.example.haemo_kotlin.util.TextEnterRowField
import com.example.haemo_kotlin.util.YesOrNoDialog
import com.example.haemo_kotlin.util.personList
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun ClubPostRegisterScreen(
    viewModel: ClubPostViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {

    val title = viewModel.title.collectAsState().value
    val description = viewModel.description.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.clubPostRegisterState.collectAsState().value
    var dialogOpen by remember { mutableStateOf(false) }
    var confirmDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }
    val mainColor by mainViewModel.colorState.collectAsState()

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<ClubPostResponseModel> -> {
                confirmDialogOpen = true
            }

            is Resource.Error<ClubPostResponseModel> -> {
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
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            PostRegisterAppBar(
                "소모임 등록",
                mainColor = mainColor,
                navController
            )
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterRowField(
                    "소모임",
                    mainColor = mainColor,
                    value = title
                ) {
                    viewModel.title.value = it
                }
                TextEnterRowField(
                    "설명",
                    mainColor = mainColor,
                    value = description
                ) {
                    viewModel.description.value = it
                }
                ClubPostInfo(viewModel, mainColor)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(null, viewModel, null, 2, mainColor, navController) {
                    dialogOpen = true
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubPostInfo(viewModel: ClubPostViewModel, mainColor: Int) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Column {
        Row(
            Modifier
                .width((screenWidth * 0.65).dp)
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ImagePickerBox(viewModel = viewModel, null, Modifier.weight(1f), 2)
            DropDownMenu(
                "0명",
                list = personList,
                mainColor = mainColor,
                modifier = Modifier
                    .weight(2f)
                    .width((screenWidth / 5).dp)
            ) {
                viewModel.person.value = personList.indexOf(it) + 1
            }
        }
    }
}

@Composable
fun ImagePickerBox(
    viewModel: ClubPostViewModel?,
    hotPlacePostViewModel: HotPlacePostViewModel?,
    modifier: Modifier,
    type: Int
) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp

    val launcher = when (type) {
        2 -> {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let { selectedImageUri ->
                    viewModel!!.uploadImage(selectedImageUri)
                }
            }
        }

        3 -> {
            rememberLauncherForActivityResult(
                contract =
                ActivityResultContracts.GetMultipleContents()
            ) { uris: List<Uri>? ->
                uris?.let { selectedImageUris ->
                    selectedImageUris.take(4).let { imageUris ->
                        hotPlacePostViewModel?.uploadImageList(imageUris)
                    }
                }
            }
        }

        else -> {
            null
        }
    }

    val imageUrl = when (type) {
        2 -> viewModel!!.image.collectAsState().value
        3 -> hotPlacePostViewModel!!.image.collectAsState().value
        else -> throw IllegalArgumentException("Invalid type: $type")
    }

    LaunchedEffect(imageUrl) {
    }

    Box(
        modifier
    ) {
        Box(
            modifier = Modifier
                .background(Color.Unspecified, RoundedCornerShape(15.dp))
                .border(1.dp, colorResource(id = R.color.mainGreyColor), RoundedCornerShape(15.dp))
                .clickable { launcher!!.launch("image/*") }
                .size(if (type == 2) (screenWidth / 3).dp else (screenWidth / 5).dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl == "") {
                Icon(
                    painter = painterResource(id = R.drawable.image_picker_icon),
                    contentDescription = "Add Image",
                    tint = Color.Gray
                )
            } else {
                Log.d("미란란", imageUrl.toString())
                if (type == 2) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Uploaded Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}