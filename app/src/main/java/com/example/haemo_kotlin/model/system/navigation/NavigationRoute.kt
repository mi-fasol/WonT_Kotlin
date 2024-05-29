package com.example.haemo_kotlin.model.system.navigation

sealed class NavigationRoutes(val route: String) {
    data object LoadingScreen : NavigationRoutes("loadingScreen")
    data object RegisterScreen : NavigationRoutes("userRegisterScreen")
    data object LoginScreen : NavigationRoutes("loginScreen")
    data object MainScreen : NavigationRoutes("mainScreen")
    data object MeetingScreen : NavigationRoutes("meetingScreen")
    data object ClubScreen : NavigationRoutes("clubScreen")
    data object HotPlaceScreen : NavigationRoutes("hotPlaceScreen")
    data object MyPageScreen : NavigationRoutes("myPageScreen")

    data object ThemeChangeScreen : NavigationRoutes("themeChangeScreen")

    data object MeetingPostDetailScreen : NavigationRoutes("meetingDetailScreen/{pId}") {
        fun createRoute(pId: Int) = "meetingDetailScreen/$pId"
    }

    data object ClubPostDetailScreen : NavigationRoutes("clubDetailScreen/{pId}") {
        fun createRoute(pId: Int) = "clubDetailScreen/$pId"
    }

    data object HotPlacePostDetailScreen : NavigationRoutes("hotPlaceDetailScreen/{pId}") {
        fun createRoute(pId: Int) = "hotPlaceDetailScreen/$pId"
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

    data object PostRegisterScreen : NavigationRoutes("postRegisterScreen")

    data object ClubPostRegisterScreen : NavigationRoutes("clubPostRegisterScreen")

    data object HotPlacePostRegisterScreen : NavigationRoutes("hotPlacePostRegisterScreen")

    data object ChatScreen : NavigationRoutes("chatScreen/{receiverId}") {
        fun createRoute(receiverId: Int) = "chatScreen/$receiverId"
    }

    data object ChatListScreen : NavigationRoutes("chatListScreen")

    data object SettingScreen : NavigationRoutes("settingScreen")

    data object WithdrawScreen : NavigationRoutes("withdrawScreen")

    data object NotificationSettingScreen : NavigationRoutes("notificationSettingScreen")

    data object ReportScreen : NavigationRoutes("reportScreen/{nickname}") {
        fun createRoute(nickname: String) = "reportScreen/$nickname"
    }

    data object InquiryScreen : NavigationRoutes("inquiryScreen")

    data object NoticeRegisterScreen : NavigationRoutes("noticeRegisterScreen")
}