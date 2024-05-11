package com.example.haemo_kotlin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.database.FirebaseDatabase

class MyBackgroundService : Service() {

    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")

    init{

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}