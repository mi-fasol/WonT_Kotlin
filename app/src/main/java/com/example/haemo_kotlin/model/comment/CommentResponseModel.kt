package com.example.haemo_kotlin.model.comment

import com.google.gson.annotations.SerializedName

data class CommentResponseModel(
    @SerializedName("cid", alternate = ["ccId", "hcId"]) val cId: Int,
    @SerializedName("pid", alternate = ["cpId", "hpId"]) val pId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)