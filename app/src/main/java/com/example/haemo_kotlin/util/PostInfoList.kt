package com.example.haemo_kotlin.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.Calendar

val personList = listOf("1명", "2명", "3명", "4명", "5명 이상")

val categoryList = listOf("친구", "술", "❤️ (Love)", "번개모임", "공부", "스터디", "공모전")

@RequiresApi(Build.VERSION_CODES.O)
val yearList = listOf("${LocalDateTime.now().year}년", ("${LocalDateTime.now().year+1}년"))

val monthList: List<String> = (1..12).map { "${it}월" }

val dayList: List<String> = (1..31).map { "${it}일" }

val hourList: List<String> = (0..23).map { "%02d시".format(it) }