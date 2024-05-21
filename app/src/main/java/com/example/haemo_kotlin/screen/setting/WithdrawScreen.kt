package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.StartActivity
import com.example.haemo_kotlin.screen.intro.UserRegisterButton
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.UserViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun WithdrawScreen(
    userViewModel: UserViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val mainColor by mainViewModel.colorState.collectAsState()
    Scaffold(
        topBar = {
            SettingScreenAppBar("계정 탈퇴", mainColor, navController = navController)
        },
        modifier = Modifier.background(Color.White)
    ) { innerPadding ->
        BoxWithConstraints {
            Column(
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                WithdrawInfo(mainColor = mainColor, userViewModel)
            }
        }
    }
}

@Composable
fun WithdrawInfo(mainColor: Int, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val nickname = SharedPreferenceUtil(context).getUser().nickname
    Box(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        Column {
            Text(
                "${nickname}님\n탈퇴하시기 전에 확인해 주세요!",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = mainColor),
                modifier = Modifier.padding(bottom = 20.dp)
            )
            WithdrawCheckNotification(mainColor)
            Row(){
            }
            WithdrawButton(userViewModel, mainColor)
        }
    }
}

@Composable
fun WithdrawCheckNotification(mainColor: Int) {
    Box(modifier = Modifier.padding(10.dp)) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colorResource(
                        id = mainColor
                    ),
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    "지금 탈퇴하시면 서비스 악용 방지를 위해 재가입이 3일 간 제한됩니다.",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.settingScreenContentTextColor)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colorResource(
                        id = mainColor
                    ),
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    "프로필, 작성글 등 모든 개인 정보가 삭제됩니다.",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.settingScreenContentTextColor)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WithdrawButton(viewModel: UserViewModel, mainColor: Int) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        }
    val isDeleted by viewModel.isDeleteSuccess.collectAsState()

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            launcher.launch(Intent(context, StartActivity::class.java))
            (context as? ComponentActivity)?.finish()
        }
    }

    Button(
        onClick = {
            viewModel.deleteUser()
        },
        enabled = true,
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
        Text("회원 탈퇴", color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}