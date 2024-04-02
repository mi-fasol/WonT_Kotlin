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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.screen.intro.LoadingScreen
import com.example.haemo_kotlin.screen.intro.LoginScreen
import com.example.haemo_kotlin.screen.intro.UserRegisterScreen
import com.example.haemo_kotlin.screen.main.MainScreen
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
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
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") {
                            MainScreen(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}