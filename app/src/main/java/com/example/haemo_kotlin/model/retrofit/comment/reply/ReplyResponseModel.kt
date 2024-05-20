package com.example.haemo_kotlin.model.retrofit.comment.reply

import com.google.gson.annotations.SerializedName

data class ReplyResponseModel(
    @SerializedName("rid", alternate = ["crId", "hrId"]) val rId: Int,
    @SerializedName("cid", alternate = ["ccId", "hcId"]) val cId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)