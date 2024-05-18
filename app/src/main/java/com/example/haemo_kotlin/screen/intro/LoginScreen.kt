package com.example.haemo_kotlin.screen.intro

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.EnterInfo
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.size((screenWidth / 2).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wont_icon),
                    contentDescription = "",
                    tint = colorResource(id = mainColor),
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                Icon(
                    painter = painterResource(id = R.drawable.wont),
                    contentDescription = "",
                    tint = colorResource(id = mainColor),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            EnterInfo(
                type = "ID",
                value = loginViewModel.id.collectAsState().value,
                mainColor = mainColor,
                onValueChange = { newValue ->
                    loginViewModel.id.value = newValue
                })
            Spacer(modifier = Modifier.height(10.dp))
            EnterInfo(
                type = "P/W",
                value = loginViewModel.pwd.collectAsState().value,
                mainColor = mainColor,
                onValueChange = { newValue ->
                    loginViewModel.pwd.value = newValue
                })
            Spacer(modifier = Modifier.height(30.dp))
            loginButton(loginViewModel, mainColor, navController)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun loginButton(loginViewModel: LoginViewModel, mainColor: Int, navController:NavController) {
    val id by loginViewModel.id.collectAsState()
    val pwd by loginViewModel.pwd.collectAsState()
    val isValid = loginViewModel.isValid.collectAsState().value
    val haveId = loginViewModel.loginUser.collectAsState().value
    val loginResult by loginViewModel.isLoginSuccess.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        }

    LaunchedEffect(loginResult) {
        if (loginResult) {
            loginViewModel.checkUserExists()
            when(haveId){
                LoginViewModel.LoginUserState.LOGIN -> {
                    navController.navigate(NavigationRoutes.RegisterScreen.route)
                }
                LoginViewModel.LoginUserState.SUCCESS -> {
                    launcher.launch(Intent(context, MainActivity::class.java))
                    (context as? ComponentActivity)?.finish()
                }
                else -> {

                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                )
            }
        }
    ) {
        Button(
            onClick = {
                loginViewModel.login(id, pwd)
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
                .fillMaxHeight(0.2f)
        ) {
            Text("로그인", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}