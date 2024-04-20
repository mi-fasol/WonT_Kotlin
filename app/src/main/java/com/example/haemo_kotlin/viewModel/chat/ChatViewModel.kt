package com.example.haemo_kotlin.viewModel.chat

import android.app.Application
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
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")

    val _receiverInfo = MutableStateFlow<UserResponseModel?>(null)
    val receiverInfo: StateFlow<UserResponseModel?> = _receiverInfo

    var chatMessages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    var fireBaseChatModel = MutableStateFlow<FireBaseChatModel?>(null)
    var userChatList = MutableStateFlow<List<String>>(emptyList())

    init {
        val userId = SharedPreferenceUtil(context).getUser().uId
        firebaseDB.reference.child("user").child(userId.toString()).get()
            .addOnSuccessListener {
                it.value?.let { it ->
                    userChatList.value = it as List<String>
                }
                Log.d("유저 채팅 정보 가져옴", userChatList.value.toString())
            }
            .addOnFailureListener {
            }
    }

    // 채팅룸 입장 시 정보 가져오기
    fun getChatRoomInfo(chatId: String, receiverId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getUserInfoById(receiverId)
                if (response.isSuccessful) {
                    val receiverInfo = response.body()
                    receiverInfo?.let { userInfo ->
                        _receiverInfo.value = userInfo
                    }
                    Log.d("미란 receiver", _receiverInfo.value.toString())
                } else {
                    Log.e("ChatViewModel", "Failed to get receiver info: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error while getting receiver info: ${e.message}")
            }
        }

        chatRef.child(chatId).get()
            .addOnSuccessListener {
                it.value?.let { value ->
                    val result = value as HashMap<String, Any>?
                    val sender = result?.get("sender") as HashMap<String, Any>?
                    val receiver = result?.get("receiver") as HashMap<String, Any>?

                    val chatModel = FireBaseChatModel(
                        result?.get("id") as String,
                        ChatUserModel(
                            (sender?.get("id") as Long).toInt(),
                            sender["nickname"] as String
                        ),
                        ChatUserModel(
                            (receiver?.get("id") as Long).toInt(),
                            receiver["nickname"] as String
                        ),
                        result["messages"] as List<ChatMessageModel>
                    )
                    fireBaseChatModel.value = chatModel
                }
            }
            .addOnFailureListener {
                Log.d("미란 chatRoom", it.toString())
            }

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatMessage = arrayListOf<ChatMessageModel>()
                val messageData = snapshot.value as ArrayList<HashMap<String, Any>>?

                messageData?.forEach {
                    chatMessage.add(
                        ChatMessageModel(
                            it["content"] as String,
                            it["isRead"] as? Boolean ?: false,
                            it["createdAt"] as Long,
                            (it["from"] as Long).toInt()
                        )
                    )
                }
                chatMessages.value = arrayListOf()
                chatMessages.value = chatMessage.toList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("미란 chatCancel", "loadMessage:onCancelled", error.toException())
            }
        }

        chatRef.child(chatId).child("messages")
            .addValueEventListener(chatListener)
    }

    fun sendMessage(
        chatId: String,
        receiverId: Int,
        chatMessageModel: ChatMessageModel
    ) {
        if (chatMessages.value.isEmpty()) {
            chatMessages.value = listOf(chatMessageModel)
            Log.d("미란 sendMessage", "새로운 채팅방 만들 거야")
            createNewChatRoom(chatId, receiverId, chatMessages.value)
        } else {
            chatMessages.value += chatMessageModel
            chatRef.child(chatId).child("messages").setValue(chatMessages.value)
                .addOnSuccessListener {
                    Log.d("미란 message", "메시지 전송 됨")
                }
                .addOnFailureListener {
                    Log.d("미란 message", it.toString())
                }
        }
    }

    // 새로운 채팅방 생성
    private fun createNewChatRoom(
        chatId: String,
        receiverId: Int,
        message: List<ChatMessageModel>
    ) {

        val myUserData = SharedPreferenceUtil(context).getUser()
        val uId = myUserData.uId
        val nickname = myUserData.nickname

        val sender = ChatUserModel(uId, nickname)
        Log.d("미란 newChatRoom sender:", sender.toString())

        viewModelScope.launch {
            if (!userChatList.value.contains(chatId)) {
                val receiverInfo = repository.getUserInfoById(receiverId)
                val receiver = ChatUserModel(receiverId, receiverInfo.body()!!.nickname)

                val fireBaseChatModel = FireBaseChatModel(chatId, sender, receiver, message)

                chatRef.child(chatId).setValue(fireBaseChatModel)
                    .addOnSuccessListener {
                        Log.d("미란 chatRoom", "채팅룸 생성 완료")
                        getChatRoomInfo(chatId, receiverId)
                    }
                    .addOnFailureListener {
                        Log.d("미란 chatRoom", it.toString())
                    }

                Log.d("미란 UserChatInfo 이전 리스트: ", userChatList.value.toString())

                userChatList.value += chatId

                Log.d("미란 UserChatInfo 추가 후 리스트: ", userChatList.value.toString())

                userRef.child(uId.toString()).setValue(userChatList.value)
                    .addOnSuccessListener {
                        Log.d("미란 UserChatInfo", userChatList.value.toString())
                        getChatRoomInfo(chatId, receiverId)
                    }
                    .addOnFailureListener {
                        Log.d("미란 UserChatInfo", it.toString())
                    }
            }
        }
    }
}