package com.example.haemo_kotlin.model.chat

//data class ChatModel(
//    val id: String? = null,
//    val writer: ChatUser,
//    val contact: ChatUser,
//    val messages: List<Chat> = listOf()
//)
//
//data class ChatUser(
//    val id: Int?,
//    val nickname: String
//)
//
//data class Chat(
//    val content: String,
//    val createdAt: Long,
//    val from: Int
//)

data class FireBaseChatModel(
    val id: String?,
    val sender: ChatUserModel,
    val receiver: ChatUserModel,
    val messages: List<ChatMessageModel>
)

data class ChatUserModel(
    val id: Int?,
    val nickname: String
)

data class ChatMessageModel(
    val content: String,
    val isRead: Boolean,
    val createdAt: Long,
    val from: Int
)