package com.example.haemo_kotlin.model.wish

import com.google.gson.annotations.SerializedName

data class WishListResponseModel(
    @SerializedName("wmId") val wId: Int? = null,
    @SerializedName("wcId") val wcId: Int? = null,
    @SerializedName("wid") val wpId: Int? = null,
    @SerializedName("pid") val pId: Int? = null,
    @SerializedName("cpId") val cpId: Int? = null,
    @SerializedName("hpId") val hpId: Int? = null,
    @SerializedName("uid") val uId: Int,
)