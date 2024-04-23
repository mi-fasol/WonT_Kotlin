package com.example.haemo_kotlin.viewModel.chat

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.MainApplication
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.example.haemo_kotlin.model.chat.ChatMessageModel
import com.example.haemo_kotlin.model.chat.ChatUserModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")

    var fireBaseChatModel = MutableStateFlow<List<FireBaseChatModel>>(emptyList())
    var userChatList = MutableStateFlow<List<String?>>(emptyList())
    var receiverList = MutableStateFlow<List<UserResponseModel?>>(emptyList())

    val uId = SharedPreferenceUtil(context).getUser().uId

    init {
        getChatList()
    }

    fun getLastChatInfo(chatId: String) {
        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let { value ->
                    val result = value as HashMap<String, Any>?
                    val sender = result?.get("sender") as HashMap<String, Any>?
                    val receiver = result?.get("receiver") as HashMap<String, Any>?
                    val _messageData = result?.get("messages") as ArrayList<HashMap<String, Any>>?

                    var messageData: ArrayList<ChatMessageModel>? = null

                    if (!_messageData.isNullOrEmpty()) {
                        _messageData.forEach { data ->
                            if (messageData.isNullOrEmpty()) {
                                messageData = arrayListOf(
                                    ChatMessageModel(
                                        createdAt = data["createdAt"] as Long,
                                        isRead = data["isRead"] as? Boolean ?: false,
                                        content = data["content"] as String,
                                        from = (data["from"] as Long).toInt()
                                    )
                                )
                            } else {
                                messageData!!.add(
                                    ChatMessageModel(
                                        createdAt = data["createdAt"] as Long,
                                        isRead = data["isRead"] as? Boolean ?: false,
                                        content = data["content"] as String,
                                        from = (data["from"] as Long).toInt()
                                    )
                                )
                            }
                        }
                    }
                    val _chatData = FireBaseChatModel(
                        result?.get("id") as String,
                        ChatUserModel(
                            (sender?.get("id") as Long).toInt(),
                            sender["nickname"] as String
                        ),
                        ChatUserModel(
                            (receiver?.get("id") as Long).toInt(),
                            receiver["nickname"] as String
                        ),
                        messageData!!.toList()
                    )
                    var id = if ((sender["id"] as Long).toInt() != uId) {
                        (sender["id"] as Long).toInt()
                    } else {
                        (receiver["id"] as Long).toInt()
                    }

                    viewModelScope.launch {
                        try {
                            val response = repository.getUserInfoById(id)
                            if (response.isSuccessful) {
                                val receiverInfo = response.body()
                                receiverInfo?.let { userInfo ->
                                    if (receiverList.value.contains(userInfo)) {
                                        receiverList.value -= userInfo
                                        receiverList.value += userInfo
                                    } else {
                                        receiverList.value += userInfo
                                    }
                                }
                                // receiverList.value = receiverList.value.asReversed()
                                Log.d("미란 receiver", receiverList.value.toString())
                            } else {
                                Log.e(
                                    "ChatListViewModel",
                                    "Failed to get receiver info: ${response.errorBody()}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e(
                                "ChatListViewModel",
                                "Error while getting receiver info: ${e.message}"
                            )
                        }
                    }

                    if (fireBaseChatModel.value.isEmpty()) {
                        fireBaseChatModel.value = listOf(_chatData)
                        Log.d("chatList 생성", fireBaseChatModel.value.toString())
                    } else {
                        fireBaseChatModel.value.forEach { firebaseChat ->
                            if (firebaseChat.id == _chatData.id) {
                                fireBaseChatModel.value -= firebaseChat
                                fireBaseChatModel.value += _chatData
                            }
                        }
                        if (!fireBaseChatModel.value.contains(_chatData)) {
                            fireBaseChatModel.value += _chatData
                        }
                        Log.d("chatList 추가", fireBaseChatModel.value.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("미란 ChatListViewModel", "loadMessage:onCancelled", error.toException())
            }
        }
        chatRef.child(chatId).addValueEventListener(chatListener)
    }

    fun checkLastMessage(chatData: FireBaseChatModel): ChatMessageModel? {
        val lastChat = if (chatData.messages.isNotEmpty()) {
            chatData.messages[chatData.messages.size - 1]
        } else {
            null
        }
        return lastChat
    }

    fun getNickname(chatData: FireBaseChatModel, myNickname: String): String {
        val nickname = if (chatData.sender.nickname == myNickname) {
            chatData.receiver.nickname
        } else {
            chatData.sender.nickname
        }
        return nickname
    }

    private fun getChatList() {
        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let {
                    val _usersChatList = it as ArrayList<String>
                    userChatList.value = _usersChatList

                    userChatList.value.forEach { chatId ->
                        getLastChatInfo(chatId!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "loadMessage:onCancelled", error.toException())
            }
        }
        userRef.child(uId.toString()).addValueEventListener(chatListener)
    }
}