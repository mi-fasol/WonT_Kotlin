package com.example.haemo_kotlin.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertDate(input: String): String {
    val parsedDate = parseDateString(input, "yyyy년 MM월 dd일 hh시")
    val formattedDate = formatDate(parsedDate, "yyyy.MM.dd hh시")

    return formattedDate
}

fun parseDateString(dateString: String, format: String): Date {
    val parser = SimpleDateFormat(format, Locale.KOREA)
    return parser.parse(dateString) ?: Date()
}

fun formatDate(date: Date, format: String): String {
    val formatter = SimpleDateFormat(format, Locale.KOREA)
    return formatter.format(date)
}