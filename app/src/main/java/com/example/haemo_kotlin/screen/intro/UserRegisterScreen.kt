package com.example.haemo_kotlin.screen.intro

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.user.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserRegisterScreen(viewModel: UserViewModel, mainViewModel: MainViewModel, navController: NavController) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val context = LocalContext.current
    val mainColor = SharedPreferenceUtil(context).getInt("themeColor", R.color.mainColor)

    Scaffold(
        topBar = {
            BackArrowAppBar("회원가입", navController)
        }
    ) {
        Column {
            Divider(thickness = 0.5.dp, color = colorResource(id = mainColor))

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
                    Icon(
                        painter = painterResource(id = R.drawable.wont),
                        contentDescription = "",
                        tint = colorResource(id = mainColor),
                        modifier = Modifier
                            .width((screenWidth / 2).dp)
                            .height((screenHeight / 7).dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("프로필", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    Spacer(Modifier.height(20.dp))
                    SelectUserImage(viewModel = viewModel, screenWidth)
                    Spacer(Modifier.height(20.dp))
                    EnterInfo(
                        type = "닉네임",
                        value = viewModel.nickname.collectAsState().value,
                        mainColor = mainColor,
                        onValueChange = { newValue ->
                            viewModel.nickname.value = newValue
                        })
                    Spacer(Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SelectDropDownMenu(
                            type = "성별",
                            text = "선택",
                            list = userGenderList,
                            modifier = Modifier.weight(1f),
                            onValueChange = { newValue ->
                                viewModel.gender.value = newValue
                            },
                            mainColor = mainColor
                        )
                        Spacer(Modifier.width(10.dp))
                        SelectDropDownMenu(
                            type = "전공",
                            text = "선택하기",
                            list = userMajorList,
                            modifier = Modifier.weight(2f),
                            onValueChange = { newValue ->
                                viewModel.major.value = newValue
                            },
                            mainColor = mainColor
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    UserRegisterButton(viewModel, mainColor, navController)
                }
            }
        }
    }
}

@Composable
fun SelectUserImage(viewModel: UserViewModel, width: Int) {
    val image = viewModel.image.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (image.value == 0) {
                viewModel.image.value = 7
            } else {
                viewModel.image.value -= 1
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Image(
            painter = painterResource(id = userProfileList[image.value]),
            contentDescription = null,
            modifier = Modifier.size((width / 2.5).dp)
        )
        IconButton(onClick = {
            if (image.value == 7) {
                viewModel.image.value = 0
            } else {
                viewModel.image.value += 1
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDropDownMenu(
    type: String,
    text: String,
    mainColor : Int,
    list: List<String>,
    modifier: Modifier,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(text) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier) {
        Text(type, fontWeight = FontWeight.SemiBold)
        androidx.compose.material3.OutlinedTextField(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    textfieldSize = coordinates.size.toSize()
                },
            value = selectedText,
            readOnly = true,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = mainColor),
                unfocusedBorderColor = colorResource(id = mainColor),
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp),
            trailingIcon = {
                Icon(
                    icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded },
                )
            }
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        list.forEach { label ->
            DropdownMenuItem(onClick = {
                selectedText = label
                onValueChange(label)
                expanded = false
            }) {
                Text(text = label)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserRegisterButton(viewModel: UserViewModel, mainColor: Int, navController: NavController) {
    val nickname by viewModel.nickname.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val major by viewModel.major.collectAsState()
    val isValid = viewModel.isValid.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val registerResult = viewModel.isRegisterSuccess.collectAsState().value
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        }

    LaunchedEffect(registerResult) {
        if (registerResult) {
            launcher.launch(Intent(context, MainActivity::class.java))
            (context as? ComponentActivity)?.finish()
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
        androidx.compose.material3.Button(
            onClick = {
                viewModel.registerUser(nickname, major, gender)
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
            Text("가입하기", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}