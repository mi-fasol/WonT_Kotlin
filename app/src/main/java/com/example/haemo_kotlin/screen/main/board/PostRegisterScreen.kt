package com.example.haemo_kotlin.screen.main.board

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.*
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.viewModel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun PostRegisterScreen(viewModel: PostViewModel, navController: NavController) {

    val title = viewModel.title.collectAsState().value
    val content = viewModel.content.collectAsState().value

    Box(
        modifier = Modifier.background(Color.White)
    ) {
        Column {
            PostRegisterAppBar("모임 등록", navController)
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                TitleEnterField("모임", title) {
                    viewModel.title.value = it
                }
                PostInfo(viewModel)
                ContentEnterField(value = content) {
                    viewModel.content.value = it
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostInfo(viewModel: PostViewModel) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp

    Column {
        Row(
            Modifier.width((screenWidth * 0.75).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PostSelectDropDownMenu(
                "0명",
                list = personList,
                modifier = Modifier
                    .weight(1f)
                    .width((screenWidth / 5).dp)
            ) {
                viewModel.person.value = personList.indexOf(it) + 1
            }
            PostSelectDropDownMenu(
                "모임 카테고리",
                list = categoryList,
                modifier = Modifier
                    .weight(1.5f)
                    .width((screenWidth / 3.5).dp)
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
            Box(
                Modifier.weight(1f)
            ) {
                PostSelectDropDownMenu(
                    "2024년",
                    list = yearList,
                    modifier = Modifier
                        .width((screenWidth / 5.5).dp)
                ) {
                    viewModel.deadlineYear.value = it
                }
            }
            Row(
                Modifier.weight(2f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    Modifier.weight(1f)
                ) {
                    PostSelectDropDownMenu(
                        "1월",
                        list = monthList,
                        modifier = Modifier.width((screenWidth / 8).dp)
                    ) {
                        viewModel.deadlineMonth.value = it
                    }
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    PostSelectDropDownMenu(
                        "1일",
                        list = dayList,
                        modifier = Modifier.width((screenWidth / 8).dp)
                    ) {
                        viewModel.deadlineDay.value = it
                    }
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    PostSelectDropDownMenu(
                        "01시",
                        list = hourList,
                        modifier = Modifier.width((screenWidth / 8).dp)
                    ) {
                        viewModel.deadlineTime.value = it
                    }
                }
            }
        }
    }
}