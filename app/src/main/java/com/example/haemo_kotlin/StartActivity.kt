package com.example.haemo_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haemo_kotlin.screen.intro.LoadingScreen
import com.example.haemo_kotlin.screen.intro.LoginScreen
import com.example.haemo_kotlin.screen.intro.UserRegisterScreen
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.viewModel.LoginViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.viewModel.UserRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
    private val viewModel by viewModels<PostViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val userRegisterViewModel by viewModels<UserRegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            Haemo_kotlinTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "loadingScreen") {
/*                    composable(NavigationRoutes.LoadingScreen.route) {
                        LoadingScreen(viewModel = loadingViewModel, navController = navController)
                    }*/
                        composable("loadingScreen") {
                            LoadingScreen(
                                loginViewModel = loginViewModel,
                                navController = navController
                            )
                        }
                        composable("loginScreen") {
                            LoginScreen(
                                loginViewModel = loginViewModel,
                                navController = navController
                            )
                        }
                        composable("userRegisterScreen") {
                            UserRegisterScreen(
                                viewModel = userRegisterViewModel,
                                navController = navController
                            )
                        }
                        composable("mainScreen") {
                            MainScreen(viewModel, loginViewModel)
                        }
                    }
                }
            }
        }
    }
}