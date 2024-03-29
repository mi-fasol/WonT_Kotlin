package com.example.haemo_kotlin

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<PostViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Haemo_kotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel, loginViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: PostViewModel, loginViewModel: LoginViewModel) {
    val selectedTab = remember { mutableIntStateOf(0) }

//    App()

    TabScreen(selectedTab = selectedTab, viewModel, loginViewModel)
}

@Composable
fun TabScreen(selectedTab: MutableState<Int>, viewModel: PostViewModel, loginViewModel: LoginViewModel) {
    val tabs = listOf("Tab 1", "Tab 2")

    Column {
        TabRow(selectedTabIndex = selectedTab.value) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index }
                ) {
                    Text(text = title)
                }
            }
        }

        when (selectedTab.value) {
            0 -> Tab1Screen(viewModel, loginViewModel)
            1 -> Tab2Screen(viewModel)
        }
    }
}

@Composable
fun Tab1Screen(viewModel: PostViewModel, loginViewModel: LoginViewModel) {
    LaunchedEffect(true) {
        viewModel.getPost()
    }

    val postList = viewModel.postModelList.collectAsState()
    Column() {
        Button(onClick = {
            loginViewModel.login("2019152028", "a0967312!")
            Log.d("로그인", loginViewModel.loginState.toString())
        }) {
        }

//        Button(onClick = {viewModel.setZero()}){}

        LazyColumn() {
            items(postList.value.size) { idx ->
                Text(postList.value[idx].title)
            }
        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun Tab2Screen(viewModel: PostViewModel) {
    val num = viewModel.num.collectAsState()
    val post = viewModel.postModel.collectAsState()
    val title = if(post.value == null) "비어있음" else post.value!!.title

    LaunchedEffect(true) {
        viewModel.getOnePost(num.value)
    }
    Column() {
        Text(num.value.toString())
        Text(title)
    }
}