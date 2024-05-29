package com.example.haemo_kotlin.model.retrofit.user

import com.google.gson.annotations.SerializedName

enum class Role { USER, ADMIN }
data class UserResponseModel(
    @SerializedName("uid") val uId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("major") val major: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("user_image") val userImage: Int,
    @SerializedName("roleType") val role: Role
)
