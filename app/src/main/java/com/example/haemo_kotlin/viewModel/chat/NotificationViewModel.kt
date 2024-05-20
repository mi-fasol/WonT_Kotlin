package com.example.haemo_kotlin.viewModel.chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.chat.FireBaseChatModel
import com.example.haemo_kotlin.model.retrofit.chat.ChatMessageModel
import com.example.haemo_kotlin.model.retrofit.chat.ChatUserModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.service.MyFirebaseMessagingService
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.google.firebase.database.ChildEventListener
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
class NotificationViewModel @Inject constructor(
    context: Context,
    private val firebaseMessagingService: MyFirebaseMessagingService
) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")
    val uId = SharedPreferenceUtil(context).getUser().uId
    private var userChatList = MutableStateFlow<List<String>>(emptyList())

    init {
        userRef.child(uId.toString()).get()
            .addOnSuccessListener {
                it.value?.let {
                    userChatList.value = it as List<String>
                }
                Log.d("유저 채팅 정보 가져옴", userChatList.value.toString())
            }
            .addOnFailureListener {
            }
        observeUserChats()
        listenToChatIds()
    }

    private fun observeUserChats() {
        userRef.child(uId.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (chatSnapshot in snapshot.children) {
                    val chatId = chatSnapshot.value.toString()
                    val parts = chatId.split("+")
                    if (parts[0] == uId.toString()) {
                        checkChatMessages(chatId)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatViewModel", "loadPost:onCancelled", error.toException())
            }
        })
    }

    private fun checkChatMessages(chatId: String) {
        chatRef.child(chatId).child("messages").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessageModel::class.java)
                if (message != null && !message.isRead && message.from != uId) {
                    firebaseMessagingService.sendNotification(message)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatViewModel", "checkChatMessages:onCancelled", error.toException())
            }
        })
    }

    private fun listenToChatIds() {
        userChatList.value.forEach { chatId ->
            chatRef.child(chatId).child("messages")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                        if (chatMessage?.from != uId) {
                            chatMessage?.let {
                                firebaseMessagingService.sendNotification(
                                    chatMessage
                                )
                            }
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error: ${error.message}")
                    }
                })
        }
    }
}