package com.example.haemo_kotlin.model

import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("uid") val uId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("major") val major: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("user_image") val userImage: Int
)
