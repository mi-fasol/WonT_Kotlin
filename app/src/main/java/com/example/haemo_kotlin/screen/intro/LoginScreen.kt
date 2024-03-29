package com.example.haemo_kotlin.screen.intro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.viewModel.LoginViewModel

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
            EnterUserId(loginViewModel)
            Spacer(modifier = Modifier.height(10.dp))
            EnterPwdField(loginViewModel)
            Spacer(modifier = Modifier.height(30.dp))
            loginButton(loginViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterUserId(loginViewModel: LoginViewModel) {
    var id = loginViewModel.id.collectAsState().value
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            Text("ID", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = id,
                onValueChange = { newId ->
                    loginViewModel.id.value = newId
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xff82C0EA),
                    unfocusedBorderColor = Color.LightGray,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPwdField(loginViewModel: LoginViewModel) {
    val pwd by loginViewModel.pwd.collectAsState()
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
        ) {
            Text("P/W", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = pwd,
                onValueChange = { newPwd ->
                    loginViewModel.pwd.value = newPwd
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xff82C0EA),
                    unfocusedBorderColor = Color.LightGray,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(15.dp)
            )
        }
    }
}

@Composable
fun loginButton(loginViewModel: LoginViewModel) {
    val id by loginViewModel.id.collectAsState()
    val pwd by loginViewModel.pwd.collectAsState()
    /*val isValid = loginViewModel.isValid.collectAsState().value*/

    Button(
        onClick = {
            loginViewModel.login(id, pwd)
        },
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