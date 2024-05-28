package com.example.haemo_kotlin.screen.main.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.retrofit.user.MailState
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ContentEnterField
import com.example.haemo_kotlin.util.InquiryTypeDialog
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.TextEnterColumnField
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.InquiryViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun InquiryScreen(
    mainViewModel: MainViewModel,
    inquiryViewModel: InquiryViewModel,
    navController: NavController
) {
    val mainColor by mainViewModel.colorState.collectAsState()

    Scaffold(
        topBar = {
            SettingScreenAppBar("문의하기", mainColor, navController = navController)
        },
        modifier = Modifier.background(Color.White)
    ) { innerPadding ->
        BoxWithConstraints(
            Modifier.background(
                Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 20.dp,
                        end = 20.dp
                    )
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                EnterInquiryInfo(inquiryViewModel)
                InquiryButton(mainColor, inquiryViewModel, navController)
            }
        }
    }
}

@Composable
fun EnterInquiryInfo(inquiryViewModel: InquiryViewModel) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp
    val content by inquiryViewModel.content.collectAsState()
    val inquiryType by inquiryViewModel.inquiryType.collectAsState()
    val email by inquiryViewModel.email.collectAsState()
    var openInquiryDialog = remember {
        mutableStateOf(false)
    }

    Column {
        TextEnterColumnField(
            title = "문의 유형",
            value = inquiryType,
            hasButton = true,
            onValueChange = {
                inquiryViewModel.inquiryType.value = it
            }) {
            openInquiryDialog.value = true
        }
        TextEnterColumnField(
            title = "답변 받을 이메일",
            value = email,
            hasButton = false,
            onValueChange = {
                inquiryViewModel.email.value = it
            }) {
        }
        Text(
            "문의 내용",
            modifier = Modifier.padding(top = 30.dp, bottom = 15.dp),
            color = colorResource(
                id = R.color.inquiryScreenTitleTextColor
            ),
            fontSize = 17.5.sp,
            fontWeight = FontWeight.SemiBold
        )
        ContentEnterField(value = content) {
            if (it.length <= 500) {
                inquiryViewModel.content.value = it
            }
        }
    }

    if (openInquiryDialog.value) {
        InquiryTypeDialog(inquiryViewModel) {
            openInquiryDialog.value = false
        }
    }
}

@Composable
fun InquiryButton(
    mainColor: Int,
    inquiryViewModel: InquiryViewModel,
    navController: NavController
) {
    val content by inquiryViewModel.content.collectAsState()
    val isValid by inquiryViewModel.isValid.collectAsState()
    val inquiryState by inquiryViewModel.inquiryState.collectAsState()
    val failDialog = remember { mutableStateOf(false) }
    val successDialog = remember { mutableStateOf(false) }

    LaunchedEffect(inquiryState) {
        when (inquiryState) {
            MailState.SUCCESS -> {
                successDialog.value = true
            }

            MailState.FAIL -> {
                failDialog.value = true
            }

            else -> {

            }
        }
    }

    Button(
        onClick = {
            inquiryViewModel.sendInquiry()
            Log.d("미란 문의", content)
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
            .padding(top = 30.dp)
    ) {
        Text(
            "문의하기",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }

    if (failDialog.value) {
        ConfirmDialog(content = "실패했습니다.\n다시 시도해 주세요.", mainColor = mainColor) {
            failDialog.value = false
        }
    }
    if (successDialog.value) {
        ConfirmDialog(content = "문의가 완료되었습니다.", mainColor = mainColor) {
            successDialog.value = false
            inquiryViewModel.inquiryType.value = ""
            inquiryViewModel.content.value = ""
            inquiryViewModel.inquiryState.value = MailState.NONE
            navController.popBackStack()
        }
    }
}