package com.example.haemo_kotlin.service

import android.annotation.SuppressLint
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.haemo_kotlin.MainApplication
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.chat.ChatMessageModel
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var context: Context
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")
    private var userChatList = MutableStateFlow<List<String>>(emptyList())

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)

        userRef.child(uId.toString()).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatId = snapshot.value as String
                userChatList.value += chatId
                listenToChatIds(uId)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatId = snapshot.value as String
                Log.d("미란 파이어베이스", "헐랭 삭제됨?")
                userChatList.value -= chatId
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }

    private fun listenToChatIds(uId: Int) {
        userChatList.value.forEach { chatId ->
            chatRef.child(chatId).child("messages").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                    if(chatMessage?.from != uId) {
                        chatMessage?.let { sendNotification(it, context) }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // 채팅 메시지 변경 사항 무시
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // 채팅 메시지 삭제 사항 무시
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // 채팅 메시지 이동 사항 무시
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error: ${error.message}")
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(lastChat: ChatMessageModel, context : Context) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.chat_icon)
            .setContentTitle(lastChat.senderNickname)
            .setContentText(lastChat.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        Log.d("미란 알림은요", notification.toString())

        with(NotificationManagerCompat.from(context)) {
            Log.d("미란 알림은요", "우와아")
            notify(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "chat_notification"
        private const val NOTIFICATION_ID = 100
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("미란 파이어베이스 토큰", token)
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("미란 파이어베이스 토큰", "sendRegistrationTokenToServer($token)")
    }
}