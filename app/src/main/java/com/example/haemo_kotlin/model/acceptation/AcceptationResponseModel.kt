package com.example.haemo_kotlin.model.acceptation

import com.google.gson.annotations.SerializedName

data class AcceptationResponseModel(
    @SerializedName("aid") val aId: Int,
    @SerializedName("uid") val uId: Int,
    @SerializedName("pid") val pId: Int,
    @SerializedName("accept") val acceptation: Boolean
)