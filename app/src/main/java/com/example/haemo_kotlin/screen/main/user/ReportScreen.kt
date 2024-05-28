package com.example.haemo_kotlin.screen.main.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.screen.setting.setting.WithdrawInfo
import com.example.haemo_kotlin.util.ConfirmDialog
import com.example.haemo_kotlin.util.ContentEnterField
import com.example.haemo_kotlin.util.DropDownMenu
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.categoryList
import com.example.haemo_kotlin.util.reportList
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.ReportState
import com.example.haemo_kotlin.viewModel.user.ReportViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ReportScreen(
    mainViewModel: MainViewModel,
    reportViewModel: ReportViewModel,
    nickname: String,
    navController: NavController
) {
    val mainColor by mainViewModel.colorState.collectAsState()

    Scaffold(
        topBar = {
            SettingScreenAppBar("신고하기", mainColor, navController = navController)
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
                        start = 40.dp,
                        end = 40.dp
                    )
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                ReportTitle(nickname = nickname, mainColor = mainColor)
                EnterReportInfo(mainColor, reportViewModel)
                ReportButton(mainColor, nickname, reportViewModel, navController)
            }
        }
    }
}

@Composable
fun ReportTitle(nickname: String, mainColor: Int) {
    Column(Modifier.padding(top = 50.dp)) {
        Text(
            nickname,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(
                id = mainColor
            ),
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            "님을 신고하시겠습니까?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(
                id = R.color.reportScreenTitleTextColor
            ),
            modifier = Modifier.padding(bottom = 30.dp)
        )
    }
}

@Composable
fun EnterReportInfo(mainColor: Int, reportViewModel: ReportViewModel) {
    val conf = LocalConfiguration.current
    val screenWidth = conf.screenWidthDp
    val content by reportViewModel.content.collectAsState()

    Column {
        DropDownMenu(
            "신고 사유를 선택해 주세요.",
            list = reportList,
            modifier = Modifier
                .width((screenWidth / 2).dp),
            mainColor
        ) {
            reportViewModel.reportReason.value = it
        }
        Spacer(Modifier.height(30.dp))
        ContentEnterField(value = content) {
            if (it.length <= 300) {
                reportViewModel.content.value = it
            }
        }
    }
}

@Composable
fun ReportButton(mainColor: Int, nickname: String, reportViewModel: ReportViewModel, navController: NavController) {
    val content by reportViewModel.content.collectAsState()
    val isValid by reportViewModel.isValid.collectAsState()
    val reportState by reportViewModel.reportState.collectAsState()
    val failDialog = remember { mutableStateOf(false) }
    val successDialog = remember { mutableStateOf(false) }

    LaunchedEffect(reportState){
        when(reportState){
            ReportState.SUCCESS -> {
                successDialog.value = true
            }
            ReportState.FAIL -> {
                failDialog.value = true
            }
            else -> {

            }
        }
    }

    Button(
        onClick = {
            reportViewModel.sendReport(nickname)
            Log.d("미란 신고", content)
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
            "등록하기",
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
        ConfirmDialog(content = "신고가 완료되었습니다.", mainColor = mainColor) {
            successDialog.value = false
            reportViewModel.reportReason.value = ""
            reportViewModel.content.value = ""
            reportViewModel.reportState.value = ReportState.NONE
            navController.popBackStack()
        }
    }
}