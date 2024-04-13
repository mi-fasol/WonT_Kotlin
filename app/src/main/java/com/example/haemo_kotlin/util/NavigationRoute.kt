package com.example.haemo_kotlin.util

sealed class NavigationRoutes(val route: String) {
    data object LoadingScreen : NavigationRoutes("loadingScreen")
    data object RegisterScreen : NavigationRoutes("userRegisterScreen")
    data object LoginScreen : NavigationRoutes("loginScreen")
    data object MainScreen : NavigationRoutes("mainScreen")
    data object MeetingScreen : NavigationRoutes("meetingScreen")
    data object ClubScreen : NavigationRoutes("clubScreen")
    data object HotPlaceScreen : NavigationRoutes("hotPlaceScreen")
    data object MyPageScreen : NavigationRoutes("myPageScreen")
    data object MeetingPostDetailScreen : NavigationRoutes("meetingDetailScreen/{pId}") {
        fun createRoute(pId: Int) = "meetingDetailScreen/$pId"
    }

    data object ClubPostDetailScreen : NavigationRoutes("clubDetailScreen/{pId}") {
        fun createRoute(pId: Int) = "clubDetailScreen/$pId"
    }

    data object MyMeetingBoardScreen : NavigationRoutes("myMeetingBoardScreen/{nickname}") {
        fun createRoute(nickname: String) = "myMeetingBoardScreen/$nickname"
    }

    data object MyWishMeetingScreen : NavigationRoutes("myWishMeetingScreen/{uId}") {
        fun createRoute(uId: Int) = "myWishMeetingScreen/$uId"
    }

    data object MyWishClubScreen : NavigationRoutes("myWishClubScreen/{uId}") {
        fun createRoute(uId: Int) = "myWishClubScreen/$uId"
    }

    data object MyWishHotPlaceScreen : NavigationRoutes("myWishHotPlaceScreen/{uId}") {
        fun createRoute(uId: Int) = "myWishHotPlaceScreen/$uId"
    }
}