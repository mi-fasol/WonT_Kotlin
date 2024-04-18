package com.example.haemo_kotlin.model.chat

data class MessageModel(
    val content : String,
    val isRead : Boolean,
    val sendTime : Long,
    val sender : Int
)