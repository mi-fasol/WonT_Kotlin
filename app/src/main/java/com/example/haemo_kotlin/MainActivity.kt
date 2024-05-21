package com.example.haemo_kotlin

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.haemo_kotlin.screen.main.board.MainScreen
import com.example.haemo_kotlin.screen.main.board.detail.ClubPostDetailScreen
import com.example.haemo_kotlin.screen.main.board.detail.HotPlacePostDetailScreen
import com.example.haemo_kotlin.screen.main.board.detail.MeetingPostDetailScreen
import com.example.haemo_kotlin.screen.main.board.list.ClubScreen
import com.example.haemo_kotlin.screen.main.board.list.HotPlaceScreen
import com.example.haemo_kotlin.screen.main.board.list.MeetingScreen
import com.example.haemo_kotlin.screen.main.board.register.ClubPostRegisterScreen
import com.example.haemo_kotlin.screen.main.board.register.HotPlacePostRegisterScreen
import com.example.haemo_kotlin.screen.main.board.register.PostRegisterScreen
import com.example.haemo_kotlin.screen.main.chat.ChatListScreen
import com.example.haemo_kotlin.screen.main.chat.ChatScreen
import com.example.haemo_kotlin.screen.setting.MyPageScreen
import com.example.haemo_kotlin.screen.setting.SettingScreen
import com.example.haemo_kotlin.screen.setting.ThemeChangeScreen
import com.example.haemo_kotlin.screen.setting.WithdrawScreen
import com.example.haemo_kotlin.screen.setting.detail.MyMeetingBoardScreen
import com.example.haemo_kotlin.screen.setting.detail.MyWishClubScreen
import com.example.haemo_kotlin.screen.setting.detail.MyWishHotPlaceScreen
import com.example.haemo_kotlin.screen.setting.detail.MyWishMeetingScreen
import com.example.haemo_kotlin.service.MyFirebaseMessagingService
import com.example.haemo_kotlin.ui.theme.Haemo_kotlinTheme
import com.example.haemo_kotlin.model.system.navigation.NavigationRoutes
import com.example.haemo_kotlin.viewModel.MainViewModel
import com.example.haemo_kotlin.viewModel.board.ClubPostViewModel
import com.example.haemo_kotlin.viewModel.board.HotPlacePostViewModel
import com.example.haemo_kotlin.viewModel.board.PostViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.CommentViewModel
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import com.example.haemo_kotlin.viewModel.chat.ChatListViewModel
import com.example.haemo_kotlin.viewModel.chat.ChatViewModel
import com.example.haemo_kotlin.viewModel.chat.NotificationViewModel
import com.example.haemo_kotlin.viewModel.user.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val notificationChannel = NotificationChannel(
            "chat_notification",
            "Chat",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        startFirebaseMessagingService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startFirebaseMessagingService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "chat_notification",
            "Chat Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for chat notifications"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun startFirebaseMessagingService() {
        val intent = Intent(this, MyFirebaseMessagingService::class.java)
        startService(intent)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<PostViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val clubPostViewModel by viewModels<ClubPostViewModel>()
    private val hotPlacePostViewModel by viewModels<HotPlacePostViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val commentViewModel by viewModels<CommentViewModel>()
    private val wishViewModel by viewModels<WishViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()
    private val chatListViewModel by viewModels<ChatListViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {
            finish()
        }
    }

    private fun logRegToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            val msg = token
            Log.d("미란 토큰", msg)
        })
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        logRegToken()

        mainViewModel.navigateToAnotherActivity.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { intent ->
                startActivity(intent)
                finish()
            }
        })

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
                            MainScreen(
                                navController,
                                mainViewModel,
                                viewModel,
                                wishViewModel,
                                clubPostViewModel,
                                hotPlacePostViewModel,
                                userViewModel
                            )
                        }
                        composable(NavigationRoutes.MeetingScreen.route) {
                            MeetingScreen(viewModel, mainViewModel, navController)
                        }
                        composable(NavigationRoutes.ClubScreen.route) {
                            ClubScreen(clubPostViewModel, mainViewModel, navController)
                        }
                        composable(NavigationRoutes.HotPlaceScreen.route) {
                            HotPlaceScreen(
                                hotPlacePostViewModel,
                                wishViewModel,
                                mainViewModel,
                                navController
                            )
                        }
                        composable(NavigationRoutes.MyPageScreen.route) {
                            MyPageScreen(userViewModel, mainViewModel, navController)
                        }
                        composable(
                            NavigationRoutes.MeetingPostDetailScreen.route,
                            arguments = listOf(
                                navArgument("pId") { type = NavType.IntType }
                            )
                        ) { entry ->
                            MeetingPostDetailScreen(
                                postViewModel = viewModel,
                                commentViewModel = commentViewModel,
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                pId = entry.arguments?.getInt("pId")!!
                            )
                        }
                        composable(
                            NavigationRoutes.ClubPostDetailScreen.route,
                            arguments = listOf(
                                navArgument("pId") { type = NavType.IntType }
                            )
                        ) { entry ->
                            ClubPostDetailScreen(
                                postViewModel = clubPostViewModel,
                                commentViewModel = commentViewModel,
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                pId = entry.arguments?.getInt("pId")!!
                            )
                        }
                        composable(
                            NavigationRoutes.HotPlacePostDetailScreen.route,
                            arguments = listOf(
                                navArgument("pId") { type = NavType.IntType }
                            )
                        ) { entry ->
                            HotPlacePostDetailScreen(
                                postViewModel = hotPlacePostViewModel,
                                commentViewModel = commentViewModel,
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                pId = entry.arguments?.getInt("pId")!!
                            )
                        }
                        composable(
                            NavigationRoutes.MyMeetingBoardScreen.route,
                            arguments = listOf(
                                navArgument("nickname") { type = NavType.StringType }
                            )
                        ) { entry ->
                            MyMeetingBoardScreen(
                                postViewModel = viewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                nickname = entry.arguments?.getString("nickname")!!
                            )
                        }
                        composable(
                            NavigationRoutes.MyWishMeetingScreen.route,
                            arguments = listOf(
                                navArgument("uId") { type = NavType.IntType }
                            )
                        ) { entry ->
                            MyWishMeetingScreen(
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(
                            NavigationRoutes.MyWishClubScreen.route, arguments = listOf(
                            navArgument("uId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            MyWishClubScreen(
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(
                            NavigationRoutes.MyWishHotPlaceScreen.route,
                            arguments = listOf(
                                navArgument("uId") { type = NavType.IntType }
                            )
                        ) { entry ->
                            MyWishHotPlaceScreen(
                                wishViewModel = wishViewModel,
                                mainViewModel = mainViewModel,
                                navController = navController,
                                uId = entry.arguments?.getInt("uId")!!
                            )
                        }
                        composable(NavigationRoutes.PostRegisterScreen.route) {
                            PostRegisterScreen(viewModel, mainViewModel, navController)
                        }
                        composable(NavigationRoutes.ClubPostRegisterScreen.route) {
                            ClubPostRegisterScreen(
                                clubPostViewModel,
                                mainViewModel,
                                navController
                            )
                        }
                        composable(NavigationRoutes.HotPlacePostRegisterScreen.route) {
                            HotPlacePostRegisterScreen(
                                hotPlacePostViewModel,
                                mainViewModel,
                                navController
                            )
                        }
                        composable(
                            NavigationRoutes.ChatScreen.route, arguments = listOf(
                            navArgument("receiverId") { type = NavType.IntType }
                        )
                        ) { entry ->
                            ChatScreen(
                                chatViewModel = chatViewModel,
                                mainViewModel = mainViewModel,
                                receiverId = entry.arguments?.getInt("receiverId")!!,
                                navController = navController
                            )
                        }
                        composable(NavigationRoutes.ChatListScreen.route) {
                            ChatListScreen(chatListViewModel, mainViewModel, navController)
                        }
                        composable(NavigationRoutes.ThemeChangeScreen.route) {
                            ThemeChangeScreen(viewModel = mainViewModel, navController)
                        }
                        composable(NavigationRoutes.SettingScreen.route) {
                            SettingScreen(mainViewModel = mainViewModel, navController)
                        }
                        composable(NavigationRoutes.WithdrawScreen.route) {
                            WithdrawScreen(userViewModel, mainViewModel, navController)
                        }
                    }
                }
            }
        }
    }
}