package com.example.haemo_kotlin.model.user

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("major") val major: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("user_image") val userImage: Int
)

