package com.example.haemo_kotlin.util

sealed class NavigationRoutes(val route: String) {
    object LoadingScreen : NavigationRoutes("loadingScreen")
    object RegisterScreen : NavigationRoutes("userRegisterScreen")
    object LoginScreen : NavigationRoutes("loginScreen")
    object MainScreen : NavigationRoutes("mainScreen")
    object MeetingScreen : NavigationRoutes("meetingScreen")
}