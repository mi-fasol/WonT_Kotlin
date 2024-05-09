package com.example.haemo_kotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.haemo_kotlin.model.chat.ChatMessageModel
import com.example.haemo_kotlin.model.chat.FireBaseChatModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

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

        // 알림 표시
        with(NotificationManagerCompat.from(context)) {
            Log.d("미란 알림은요", "우와아")
            notify(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "chat_notification"
        private const val NOTIFICATION_ID = 100
    }
}