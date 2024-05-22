package com.example.haemo_kotlin.screen.setting

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.StartActivity
import com.example.haemo_kotlin.util.SettingScreenAppBar
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.YesOrNoDialog
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
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                WithdrawInfo(mainColor = mainColor, userViewModel)
            }
        }
    }
}

@Composable
fun WithdrawInfo(mainColor: Int, userViewModel: UserViewModel) {
    val isChecked = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val nickname = SharedPreferenceUtil(context).getUser().nickname
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(Color.White)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked.value, onCheckedChange = {
                        isChecked.value = !isChecked.value
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = colorResource(id = mainColor).copy(alpha = 0.5f),
                        checkedColor = colorResource(id = mainColor)
                    )
                )
                Text(
                    "안내사항을 모두 확인했습니다.",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.settingScreenContentTextColor)
                )
            }
            WithdrawButton(userViewModel, mainColor, isChecked)
        }
    }
}

@Composable
fun WithdrawCheckNotification(mainColor: Int) {
    Box(
        modifier = Modifier
            .background(colorResource(id = mainColor).copy(alpha = 0.07f))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .padding(13.dp)
                .fillMaxHeight(0.17f)
        ) {
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
                    fontSize = 14.sp,
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
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.settingScreenContentTextColor)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WithdrawButton(viewModel: UserViewModel, mainColor: Int, isChecked: MutableState<Boolean>) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        }
    val isDeleted by viewModel.isDeleteSuccess.collectAsState()
    val dialogOpen = remember { mutableStateOf(false) }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            launcher.launch(Intent(context, StartActivity::class.java))
            (context as? ComponentActivity)?.finish()
        }
    }

    Button(
        onClick = {
            dialogOpen.value = true
        },
        enabled = isChecked.value,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = mainColor),
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray,
            disabledContentColor = Color.White,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text("회원 탈퇴", color = Color.White, fontWeight = FontWeight.SemiBold)
    }

    if (dialogOpen.value) {
        YesOrNoDialog(
            content = "탈퇴하시겠습니까?",
            mainColor = mainColor,
            onClickCancel = { dialogOpen.value = false }) {
            viewModel.deleteUser()
        }
    }
}