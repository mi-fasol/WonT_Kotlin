package com.example.haemo_kotlin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.haemo_kotlin.R

val sc_dream = FontFamily(
    Font(R.font.sc_dream1, FontWeight.W100, FontStyle.Normal),
    Font(R.font.sc_dream2, FontWeight.W200, FontStyle.Normal),
    Font(R.font.sc_dream3, FontWeight.W300, FontStyle.Normal),
    Font(R.font.sc_dream4, FontWeight.W400, FontStyle.Normal),
    Font(R.font.sc_dream5, FontWeight.W500, FontStyle.Normal),
    Font(R.font.sc_dream6, FontWeight.W600, FontStyle.Normal),
    Font(R.font.sc_dream7, FontWeight.W700, FontStyle.Normal),
    Font(R.font.sc_dream8, FontWeight.W800, FontStyle.Normal),
    Font(R.font.sc_dream9, FontWeight.W900, FontStyle.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val boardDetailTitle = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val boardDetailContent = TextStyle(
    fontSize = 13.sp,
    fontFamily = sc_dream,
    fontWeight = FontWeight.W400,
)

val boardDetailAttendeeInfo = TextStyle(
    fontWeight = FontWeight.W700,
    fontFamily = sc_dream,
    fontSize = 13.sp
)

val boardDetailAttendButtonText = TextStyle(
    fontSize = 9.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream,
    textAlign = TextAlign.Center
)

val commentInfo = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val commentUserNickname = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val postUserNickname = TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val postUserInfo = TextStyle(
    fontSize = 8.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)


val commentContent = TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val replyButtonText = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val buttonText = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val registerScreenProfileText = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.W700,
    fontFamily = sc_dream
)

val textFieldContent = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val textFieldTitle = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val meetingScreenNoticeText = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val meetingScreenTitle = TextStyle(
    fontSize = 13.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val meetingScreenPerson = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val meetingScreenDeadline = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.W200,
    fontFamily = sc_dream
)

val meetingScreenAttendInfo = TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W700,
    fontFamily = sc_dream
)

val clubScreenTitle = TextStyle(
    fontSize = 13.5.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val clubScreenDescription = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val boardAppBar = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val hotPlaceScreenInfo = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val popularPlaceTitle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val hotPlaceScreenTitle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val noticeScreenDate = TextStyle(
    fontSize = 11.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val settingScreenAppBar = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val bacKArrowAppBar = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val mainPageAppBar = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val boardRegisterAppBar = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val chatRoomAppBar = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val boardRegisterDropdown = TextStyle(
    fontSize = 11.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val contentEnterText = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream,
    color = Color(0xFFB6B6B6)
)

val enterField = TextStyle(
    fontSize = 17.5.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val chatMessage = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val reportedNickname = TextStyle(
    fontSize = 30.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val reportCheck = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val myWishInfo = TextStyle(
    fontSize = 17.sp,
    fontWeight = FontWeight.W700,
    fontFamily = sc_dream
)

val switchText = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val settingTitle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val withdrawInfo = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val withdrawCheck = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val myPageProfile = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.W700,
    fontFamily = sc_dream
)

val myPageNickname = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.W300,
    fontFamily = sc_dream
)

val myPageListItem = TextStyle(
    fontSize = 16.5.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream
)

val commentText = TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W400,
    fontFamily = sc_dream,
    color = Color(0xFF393939)
)

val floatingButtonText = TextStyle(
    fontSize = 11.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)