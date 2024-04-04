package com.example.haemo_kotlin.model.post

import com.example.haemo_kotlin.model.HangulUtils
import com.google.gson.annotations.SerializedName

data class HotPlacePostModel(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("address") val address: Int,
    @SerializedName("date") val date: String,
    @SerializedName("wishing") val wish: Int,
)