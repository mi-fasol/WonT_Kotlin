package com.example.haemo_kotlin.model.comment

import com.google.gson.annotations.SerializedName

data class CommentResponseModel(
    @SerializedName("cid") val cId: Int,
    @SerializedName("pid") val pId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)