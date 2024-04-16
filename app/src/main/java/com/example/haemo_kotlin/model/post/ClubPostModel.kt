package com.example.haemo_kotlin.model.post

import com.example.haemo_kotlin.util.HangulUtils
import com.google.gson.annotations.SerializedName

data class ClubPostModel(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("person") val person: Int,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("logo") val image: String?,
    @SerializedName("wishClubCnt") val wish: Int,
)

fun ClubPostModel.containsHangulSearch(searchText: String): Boolean {
    if (searchText.isBlank()) return true

    val filteredText = searchText.trim()
    val isInitialSearch = filteredText.all { it.isHangul() }

    val initials = if (isInitialSearch) {
        filteredText.map { char ->
            HangulUtils.getHangulInitialSound(char.toString())
        }.joinToString("")
    } else {
        filteredText
    }

    val title = this.title.trim()
    val titleInitials = HangulUtils.getHangulInitialSound(title)

    val containsSearchText = title.contains(filteredText, ignoreCase = true)
    val containsInitials = title.contains(initials, ignoreCase = true)
    val containsInitialLastChar =
        if (isInitialSearch && filteredText.isNotEmpty() && filteredText.last().isHangul()) {
            val lastCharInitial =
                HangulUtils.getHangulInitialSound(filteredText.last().toString())
            titleInitials.contains(lastCharInitial, ignoreCase = true)
        } else {
            true
        }

    val startsWithInitials =
        if (isInitialSearch && filteredText.isNotEmpty() && filteredText.first().isHangul()) {
            val firstCharInitial = HangulUtils.getHangulInitialSound(filteredText.first().toString())
            titleInitials.startsWith(firstCharInitial, ignoreCase = true)
        } else {
            true
        }

    return containsSearchText || containsInitials || containsInitialLastChar || startsWithInitials
}

fun Char.isHangul() = this in '\uAC00'..'\uD7A3'
