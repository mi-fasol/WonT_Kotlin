package com.example.haemo_kotlin.model

object HangulUtils {
    private val choList = arrayOf(
        "ㄱ",
        "ㄲ",
        "ㄴ",
        "ㄷ",
        "ㄸ",
        "ㄹ",
        "ㅁ",
        "ㅂ",
        "ㅃ",
        "ㅅ",
        "ㅆ",
        "ㅇ",
        "ㅈ",
        "ㅉ",
        "ㅊ",
        "ㅋ",
        "ㅌ",
        "ㅍ",
        "ㅎ"
    )
    private val initialChoCode = 44032 // 유니코드 한글 시작 번호
    private val choSungCount = 19 // 초성 개수

    fun getHangulInitialSound(word: String): String {
        val result = StringBuilder()
        for (i in 0 until word.length) {
            val char = word[i]
            if (char in '가'..'힣') {
                val code = (char.toInt() - initialChoCode) / (21 * 28)
                result.append(choList[code])
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }
}