package com.example.haemo_kotlin.screen.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.EnterInfo
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp


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
            Spacer(modifier = Modifier.height(30.dp))
            EnterInfo(
                type = "ID",
                value = loginViewModel.id.collectAsState().value,
                onValueChange = { newValue ->
                    loginViewModel.id.value = newValue
                })
            Spacer(modifier = Modifier.height(10.dp))
            EnterInfo(
                type = "P/W",
                value = loginViewModel.pwd.collectAsState().value,
                onValueChange = { newValue ->
                    loginViewModel.pwd.value = newValue
                })
            Spacer(modifier = Modifier.height(30.dp))
            loginButton(loginViewModel, navController)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun loginButton(loginViewModel: LoginViewModel, navController:NavController) {
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
                else -> {
                    launcher.launch(Intent(context, MainActivity::class.java))
                    (context as? ComponentActivity)?.finish()
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
                containerColor = Color(0xff82C0EA),
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