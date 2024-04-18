package com.example.haemo_kotlin.viewModel.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: UserRepository
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")

    val _receiverInfo = MutableStateFlow<UserResponseModel?>(null)
    val receiverInfo : StateFlow<UserResponseModel?> = _receiverInfo

    var chatMessages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    var fireBaseChatModel = MutableStateFlow<FireBaseChatModel?>(null)

    fun getChatRoomInfo(chatId: String, receiverId: Int) {
        viewModelScope.launch {
            try {
                // receiverId를 사용하여 사용자 정보를 가져옴
                val response = repository.getUserInfoById(receiverId)
                if (response.isSuccessful) {
                    val receiverInfo = response.body()
                    receiverInfo?.let { userInfo ->
                        // 가져온 사용자 정보를 MutableStateFlow에 업데이트
                        _receiverInfo.value = userInfo
                    }
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
        chatMessageModel: ChatMessageModel,
        context: Context
    ) {

        if (chatMessages.value.isEmpty()) {
            chatMessages.value = listOf(chatMessageModel)
            createNewChatRoom(chatId, receiverId, chatMessages.value, context)
        } else {
            chatMessages.value += chatMessageModel
            chatRef.child(chatId).child("messages")
                .setValue(chatMessages.value)
                .addOnSuccessListener {
                    Log.d("미란 message", "메시지 전송 됨")
                }
                .addOnFailureListener {
                    Log.d("미란 message", it.toString())
                }
        }
    }

    private fun createNewChatRoom(
        chatId: String,
        receiverId: Int,
        message: List<ChatMessageModel>,
        context: Context
    ) {

        val uId = SharedPreferenceUtil(context).getInt("uId", 0)
        var usersChatList: List<String> = emptyList()

        viewModelScope.launch {
            viewModelScope.async {
                userRef.child(uId.toString()).get()
                    .addOnSuccessListener {
                        it.value?.let { data ->
                            usersChatList = data as List<String>
                        }
                        Log.d("미란 ChatList", usersChatList.toString())
                    }
                    .addOnFailureListener {
                        Log.d(
                            "미란 ChatList", it.toString()
                        )
                    }
            }.await()

            val nickname = SharedPreferenceUtil(context).getString("nickname", "")
            val sender = ChatUserModel(uId, nickname.toString())

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

            if (usersChatList.isEmpty()) {
                usersChatList = listOf(chatId)
            } else {
                usersChatList.plus(chatId)
            }

            Log.d("미란 UserChatInfo 이전 리스트: ", usersChatList.toString())

            firebaseDB.reference.child("user").child(uId.toString()).setValue(usersChatList)
                .addOnSuccessListener {
                    Log.d("미란 UserChatInfo", usersChatList.toString())
                    getChatRoomInfo(chatId, receiverId)
                }
                .addOnFailureListener {
                    Log.d("미란 UserChatInfo", it.toString())
                }
        }
    }
}