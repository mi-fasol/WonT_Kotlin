package com.example.haemo_kotlin.model.retrofit.post

import com.google.gson.annotations.SerializedName

data class NoticeResponseModel(
    @SerializedName("nid") val nId: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("type") val category: String,
    @SerializedName("visible") val visibility: Boolean,
    @SerializedName("date") val date: String,
)
