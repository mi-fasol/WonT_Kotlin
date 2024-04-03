package com.example.haemo_kotlin.model.post

import com.google.gson.annotations.SerializedName

data class PostModel(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("person") val person: Int,
    @SerializedName("deadline") val deadline: String,
    @SerializedName("category") val category: String,
    @SerializedName("date") val date: String,
    @SerializedName("wishCnt") val wish: Int,
)
