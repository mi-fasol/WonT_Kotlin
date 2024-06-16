package com.example.haemo_kotlin.viewModel.chat

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.chat.ChatMessageModel
import com.example.haemo_kotlin.model.retrofit.chat.ChatUserModel
import com.example.haemo_kotlin.model.retrofit.chat.FireBaseChatModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.service.MyFirebaseMessagingService
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val repository: UserRepository,
    context: Context
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")

    var fireBaseChatModel = MutableStateFlow<List<FireBaseChatModel>>(emptyList())
    var userChatList = MutableStateFlow<List<String?>>(emptyList())
    var _chatList = MutableStateFlow<MutableMap<String, UserResponseModel>>(HashMap())
    var chatList: StateFlow<MutableMap<String, UserResponseModel>> = _chatList

    val uId = SharedPreferenceUtil(context).getUser().uId
    val myNickname = SharedPreferenceUtil(context).getUser().nickname

    init {
        getChatList()
    }

    fun getLastChatInfo(chatId: String) {
        val orderedChatList =
            chatRef.child(chatId).child("messages").orderByChild("createdAt").limitToLast(1)

        Log.d("미란", "하아 씨발 들어왔는데 왜 $chatId")

        orderedChatList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let { value ->
                    if (snapshot.exists()) {
                        val lastMessage =
                            snapshot.children.firstOrNull()?.value as HashMap<String, Any?>
                        Log.d("미란 lastMessage", lastMessage.toString())
                        val chatUsers = chatId.split("+")
                        var id = 0
                        var receiver = 0
                        var sender = 0
                        var senderNickname = ""
                        var receiverNickname = ""

                        if (chatUsers[0].toInt() != uId) {
                            id = chatUsers[0].toInt()
                            receiver = id
                            sender = uId
                        } else {
                            id = chatUsers[1].toInt()
                            receiver = uId
                            sender = id
                        }

                        viewModelScope.launch {
                            try {
                                val response = repository.getUserInfoById(id)
                                if (response.isSuccessful) {
                                    val receiverInfo = response.body()
                                    receiverInfo?.let { userInfo ->
                                        _chatList.value[chatId] = userInfo
                                        if (userInfo.uId == sender) {
                                            senderNickname = userInfo.nickname
                                            receiverNickname = myNickname
                                        } else {
                                            senderNickname = myNickname
                                            receiverNickname = userInfo.nickname
                                        }
                                    }
                                    Log.d("미란 chatList 해시맵", _chatList.value.toString())
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

                        var messageData: ArrayList<ChatMessageModel>? = null

                        if (lastMessage.isNotEmpty()) {
                            messageData = arrayListOf(
                                ChatMessageModel(
                                    createdAt = (lastMessage["createdAt"] as Long),
                                    isRead = lastMessage["read"] as? Boolean ?: false,
                                    content = lastMessage["content"] as String,
                                    from = (lastMessage["from"] as Long).toInt(),
                                    senderNickname = lastMessage["senderNickname"] as String
                                )
                            )
                        }

                        val _chatData = messageData?.let {
                            FireBaseChatModel(
                                chatId,
                                ChatUserModel(
                                    sender,
                                    senderNickname
                                ),
                                ChatUserModel(
                                    receiver,
                                    receiverNickname
                                ),
                                it
                            )
                        }

                        if (fireBaseChatModel.value.isEmpty()) {
                            fireBaseChatModel.value = listOf(_chatData!!)
                            Log.d("chatList 생성", fireBaseChatModel.value.toString())
                        } else {
                            fireBaseChatModel.value.forEach { firebaseChat ->
                                if (!fireBaseChatModel.value.contains(_chatData)) {
                                    var newChatModel = fireBaseChatModel.value.filter {
                                        it.id != chatId
                                    }
                                    newChatModel = newChatModel.asReversed()
                                    fireBaseChatModel.value = newChatModel
                                    Log.d(
                                        "미란 새로 되나? before ",
                                        fireBaseChatModel.value.toString()
                                    )
                                    fireBaseChatModel.value += _chatData!!
                                    Log.d("미란 새로 되나?", fireBaseChatModel.value.toString())
                                    val currentChatModels =
                                        fireBaseChatModel.value.toMutableList()
                                    currentChatModels.reverse()
                                    fireBaseChatModel.value = currentChatModels.toList()
                                    Log.d(
                                        "미란 새로 되나? reversed: ",
                                        fireBaseChatModel.value.toString()
                                    )
                                }
                            }
                            if (!fireBaseChatModel.value.contains(_chatData)) {
                                fireBaseChatModel.value += _chatData!!
                            }
                            Log.d("chatList 추가", fireBaseChatModel.value.toString())
                        }
                    } else {
                        Log.d("미란", "No message found")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("미란 ChatListViewModel", "loadMessage:onCancelled", error.toException())
            }
        }
        )
    }

    fun getChatList() {
        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let {
                    val _usersChatList = it as ArrayList<String>
                    userChatList.value = _usersChatList
                    Log.d("미란 채팅 리스트는요: ", userChatList.value.toString())
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