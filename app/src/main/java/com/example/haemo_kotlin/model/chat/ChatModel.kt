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
    var id: String?,
    var sender: ChatUserModel,
    var receiver: ChatUserModel,
    var messages: List<ChatMessageModel>
)

data class ChatUserModel(
    var id: Int?,
    var nickname: String
)

data class ChatMessageModel(
    var content: String = "",
    var createdAt: Long = 0,
    var from: Int = 0,
    var senderNickname : String = "",
    var isRead: Boolean = false
)