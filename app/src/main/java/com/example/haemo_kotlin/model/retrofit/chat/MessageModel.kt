package com.example.haemo_kotlin.model.retrofit.chat

data class MessageModel(
    val content : String,
    val isRead : Boolean,
    val sendTime : Long,
    val sender : Int
)