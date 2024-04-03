package com.example.haemo_kotlin.model.post

import com.google.gson.annotations.SerializedName

data class ClubPostResponseModel(
    @SerializedName("cpId") val pId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("person") val person: Int,
    @SerializedName("description") val category: String,
    @SerializedName("image") val image: String?,
    @SerializedName("wishClubCnt") val wish: Int
)