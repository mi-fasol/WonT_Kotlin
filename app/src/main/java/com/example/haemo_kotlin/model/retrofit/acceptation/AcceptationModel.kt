package com.example.haemo_kotlin.model.retrofit.acceptation

import com.google.gson.annotations.SerializedName

data class AcceptationModel(
    @SerializedName("uid") val uId: Int,
    @SerializedName("pid") val pId: Int,
    @SerializedName("accept") val acceptation: Boolean
)