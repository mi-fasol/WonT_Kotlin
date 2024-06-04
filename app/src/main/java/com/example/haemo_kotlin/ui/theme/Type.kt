package com.example.haemo_kotlin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.res.colorResource
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

val commentUserNickname =  TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val postUserNickname =  TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W600,
    fontFamily = sc_dream
)

val postUserInfo =  TextStyle(
    fontSize = 8.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)


val commentContent =  TextStyle(
    fontSize = 12.5.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)

val replyButtonText =  TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.W500,
    fontFamily = sc_dream
)