@file:Suppress("IMPLICIT_CAST_TO_ANY")

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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.HotPlacePostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun ClubPostRegisterScreen(viewModel: ClubPostViewModel, navController: NavController) {

    val title = viewModel.title.collectAsState().value
    val description = viewModel.description.collectAsState().value
    val content = viewModel.content.collectAsState().value
    val postRegisterState = viewModel.clubPostRegisterState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(postRegisterState) {
        when (postRegisterState) {
            is Resource.Success<ClubPostResponseModel> -> {
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
            PostRegisterAppBar("소모임 등록", navController)
            Column(modifier = Modifier.padding(horizontal = 40.dp)) {
                TextEnterField("소모임", title) {
                    viewModel.title.value = it
                }
                TextEnterField("모임", description) {
                    viewModel.description.value = it
                }
                ClubPostInfo(viewModel)
                ContentEnterField(value = content) {
                    if (it.length <= 300) {
                        viewModel.content.value = it
                    }
                }
                PostRegisterButton(null, viewModel, null, 2, navController) {
                    viewModel.registerPost(context)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClubPostInfo(viewModel: ClubPostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Column {
        Row(
            Modifier.width((screenWidth * 0.65).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ImagePickerBox(viewModel = viewModel, null, Modifier.weight(1f), 2)
            PostSelectDropDownMenu(
                "0명",
                list = personList,
                modifier = Modifier
                    .weight(2f)
                    .width((screenWidth / 5).dp)
            ) {
                viewModel.person.value = personList.indexOf(it) + 1
            }
        }
    }
}

//@Composable
//fun ImagePickerBox(viewModel: ClubPostViewModel) {
//    val context = LocalContext.current
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { selectedImageUri ->
//            viewModel.uploadImage(selectedImageUri)
//        }
//    }
//
//    val imageUrl by viewModel.imageUrl.observeAsState()
//
//    Box(
//        modifier = Modifier
//            .background(Color.LightGray, RoundedCornerShape(15.dp))
//            .clickable { launcher.launch("image/*") }
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        imageUrl?.let { url ->
//            Image(
//                painter = rememberAsyncImagePainter(url),
//                contentDescription = "Uploaded Image",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(15.dp)),
//                contentScale = ContentScale.FillBounds
//            )
//        } ?: Icon(
//            painter = painterResource(id = R.drawable.image_picker_icon),
//            contentDescription = "Add Image",
//            tint = Color.Gray
//        )
//    }
//}

//@Composable
//fun ImagePickerBox(viewModel: ClubPostViewModel, modifier: Modifier) {
//    val conf = LocalConfiguration.current
//    val screenWidth = conf.screenWidthDp
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { selectedImageUri ->
//            viewModel.uploadImage(selectedImageUri)
//        }
//    }
//    val imageUrl = viewModel.image.collectAsState().value
//
//    LaunchedEffect(imageUrl) {
//
//    }
//
//    Box(
//        modifier
//    ) {
//        Box(
//            modifier = Modifier
//                .background(Color.Unspecified, RoundedCornerShape(15.dp))
//                .clickable { launcher.launch("image/*") }
//                .width((screenWidth / 2.5).dp)
//                .height((screenWidth / 2.5).dp),
//            contentAlignment = Alignment.Center
//        ) {
//            if (imageUrl == "") {
//                Icon(
//                    painter = painterResource(id = R.drawable.accept_user_icon),
//                    contentDescription = "Add Image",
//                    tint = Color.Gray
//                )
//            } else {
//                Log.d("미란란", imageUrl)
//                Image(
//                    painter = rememberAsyncImagePainter(imageUrl),
//                    contentDescription = "Uploaded Image",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(15.dp)),
//                    contentScale = ContentScale.FillBounds
//                )
//            }
//        }
//    }
//}

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
                    painter = painterResource(id = R.drawable.accept_user_icon),
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