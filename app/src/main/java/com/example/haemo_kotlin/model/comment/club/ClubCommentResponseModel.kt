package com.example.haemo_kotlin.model.comment.club

import com.google.gson.annotations.SerializedName

data class ClubCommentResponseModel(
    @SerializedName("ccId") val cId: Int,
    @SerializedName("cpId") val pId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)