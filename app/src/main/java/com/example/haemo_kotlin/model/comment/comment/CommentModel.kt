package com.example.haemo_kotlin.model.comment.comment

import com.google.gson.annotations.SerializedName

data class CommentModel(
    @SerializedName("pid") val pId: Int? = null,
    @SerializedName("cpId") val cpId: Int? = null,
    @SerializedName("hpId") val hpId: Int? = null,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)
