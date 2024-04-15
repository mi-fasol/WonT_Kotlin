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
import com.example.haemo_kotlin.screen.main.board.ClubPostDetailScreen
import com.example.haemo_kotlin.screen.main.board.ClubScreen
import com.example.haemo_kotlin.screen.main.board.HotPlaceScreen
import com.example.haemo_kotlin.screen.main.board.MainScreen
import com.example.haemo_kotlin.screen.main.board.MeetingPostDetailScreen
import com.example.haemo_kotlin.screen.main.board.MeetingScreen
import com.example.haemo_kotlin.screen.main.board.PostRegisterScreen
import com.example.haemo_kotlin.screen.setting.MyMeetingBoardScreen
import com.example.haemo_kotlin.screen.setting.MyPageScreen
import com.example.haemo_kotlin.screen.setting.MyWishClubScreen
import com.example.haemo_kotlin.screen.setting.MyWishHotPlaceScreen
import com.example.haemo_kotlin.screen.setting.MyWishMeetingScreen
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.util.NavigationRoutes
import com.example.haemo_kotlin.viewModel.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.CommentViewModel
import com.example.haemo_kotlin.viewModel.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.PostViewModel
import com.example.haemo_kotlin.viewModel.UserViewModel
import com.example.haemo_kotlin.viewModel.WishViewModel
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
    private val wishViewModel by viewModels<WishViewModel>()

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
                            MainScreen(navController, viewModel, clubPostViewModel, hotPlacePostViewModel, userViewModel)
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
                        composable(NavigationRoutes.MeetingPostDetailScreen.route,
                            arguments = listOf(
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
                        composable(NavigationRoutes.ClubPostDetailScreen.route, arguments = listOf(
                            navArgument("pId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            ClubPostDetailScreen(
                                postViewModel = clubPostViewModel,
                                commentViewModel = commentViewModel,
                                navController = navController,
                                pId = entry.arguments?.getInt("pId")!!
                            )
                        }
                        composable(NavigationRoutes.MyMeetingBoardScreen.route, arguments = listOf(
                            navArgument("nickname") { type = NavType.StringType }
                        )
                        ) { entry ->
                            MyMeetingBoardScreen(
                                postViewModel = viewModel,
                                navController = navController,
                                nickname = entry.arguments?.getString("nickname")!!
                            )
                        }
                        composable(NavigationRoutes.MyWishMeetingScreen.route, arguments = listOf(
                            navArgument("uId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            MyWishMeetingScreen(
                                wishViewModel = wishViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(NavigationRoutes.MyWishClubScreen.route, arguments = listOf(
                            navArgument("uId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            MyWishClubScreen(
                                wishViewModel = wishViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(NavigationRoutes.MyWishHotPlaceScreen.route, arguments = listOf(
                            navArgument("uId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            MyWishHotPlaceScreen(
                                wishViewModel = wishViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(NavigationRoutes.PostRegisterScreen.route) {
                            PostRegisterScreen(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}