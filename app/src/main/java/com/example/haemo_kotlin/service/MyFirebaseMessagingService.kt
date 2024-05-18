package com.example.haemo_kotlin.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.R
import com.example.haemo_kotlin.model.chat.ChatMessageModel
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var context: Context
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")
    private var userChatList = MutableStateFlow<List<String>>(emptyList())
    private val chatListeners = mutableMapOf<String, ChildEventListener>()

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)
        newChatRoomCreated(uId)
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(lastChat: ChatMessageModel) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.chat_icon)
            .setContentTitle(lastChat.senderNickname)
            .setContentText(lastChat.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        Log.d("미란 알림은요", notification.toString())

        with(NotificationManagerCompat.from(context)) {
            Log.d("미란 알림은요", "우와아")
            val notificationId = System.currentTimeMillis().toInt()
            notify(notificationId, notification)
        }
    }

    private fun newChatRoomCreated(uId: Int) {
        userRef.child(uId.toString()).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatId = snapshot.value as String
                if (chatId !in userChatList.value) {
                    userChatList.value += chatId
                    addChatListener(chatId, uId)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatId = snapshot.value as String
                userChatList.value -= chatId
                removeChatListener(chatId)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }

    private fun addChatListener(chatId: String, uId: Int) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                if (chatMessage?.from != uId && chatMessage?.createdAt == System.currentTimeMillis()) {
                    sendNotification(chatMessage)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        }
        chatRef.child(chatId).child("messages").addChildEventListener(listener)
        chatListeners[chatId] = listener
    }

    private fun removeChatListener(chatId: String) {
        chatListeners[chatId]?.let {
            chatRef.child(chatId).child("messages").removeEventListener(it)
        }
        chatListeners.remove(chatId)
    }

    companion object {
        private const val CHANNEL_ID = "chat_notification"
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