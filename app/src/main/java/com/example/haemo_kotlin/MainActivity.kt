package com.example.haemo_kotlin

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.haemo_kotlin.screen.main.ClubScreen
import com.example.haemo_kotlin.screen.main.HotPlaceScreen
import com.example.haemo_kotlin.screen.main.MainScreen
import com.example.haemo_kotlin.screen.main.MeetingPostDetailScreen
import com.example.haemo_kotlin.screen.main.MeetingScreen
import com.example.haemo_kotlin.screen.setting.MyPageScreen
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.viewModel.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.CommentViewModel
import com.example.haemo_kotlin.viewModel.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.LoginViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.viewModel.UserViewModel
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
    private val clubPostViewModel by viewModels<ClubPostViewModel>()
    private val hotPlacePostViewModel by viewModels<HotPlacePostViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        setContent {
            Haemo_kotlinTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable(NavigationRoutes.MainScreen.route) {
                            MainScreen(navController, viewModel)
                        }
                        composable(NavigationRoutes.MeetingScreen.route) {
                            MeetingScreen(viewModel, navController)
                        }
                        composable(NavigationRoutes.ClubScreen.route) {
                            ClubScreen(clubPostViewModel, navController)
                        }
                        composable(NavigationRoutes.HotPlaceScreen.route) {
                            HotPlaceScreen(hotPlacePostViewModel, navController)
                        }
                        composable(NavigationRoutes.MyPageScreen.route) {
                            MyPageScreen(userViewModel, navController)
                        }
                        composable(NavigationRoutes.MeetingPostDetailScreen.route, arguments = listOf(
                            navArgument("pId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            MeetingPostDetailScreen(
                                postViewModel = viewModel,
                                commentViewModel = commentViewModel,
                                navController = navController,
                                pId = entry.arguments?.getInt("pId")!!
                            )
                        }
                    }
                }
            }
        }
    }
}