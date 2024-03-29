package com.example.haemo_kotlin.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(loginViewModel: LoginViewModel, navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val loginUser by loginViewModel.loginUser.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.size((screenWidth / 2).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.wont_icon),
                contentDescription = "",
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
            )
            Image(
                painter = painterResource(id = R.drawable.wont),
                contentDescription = "",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
        }
    }

    LaunchedEffect(true) {
        loginViewModel.checkUserExists(context)
        delay(1500)
        when (loginUser) {
            LoginViewModel.LoginUserState.NONE -> {
                navController.navigate("loginScreen")
            }
            LoginViewModel.LoginUserState.LOGIN -> {
                navController.navigate("userRegisterScreen")
            }
            else -> {
                navController.navigate("mainScreen")
            }
        }
    }
}