package com.example.haemo_kotlin.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel

class SharedPreferenceUtil(context: Context) {
    private val PREF_NAME = "MyPrefs"
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, 0)

    fun setString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue) ?: defaultValue
    }

    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun setUser(user: UserResponseModel) {
        prefs.edit().putInt("uId", user.uId).apply()
        prefs.edit().putString("nickname", user.nickname).apply()
        prefs.edit().putString("studentId", user.studentId.toString()).apply()
        prefs.edit().putString("major", user.major).apply()
        prefs.edit().putString("gender", user.gender).apply()
        prefs.edit().putInt("image", user.userImage).apply()
    }

    fun getUser(): UserResponseModel {
        return UserResponseModel(
            prefs.getInt("uId", 0),
            prefs.getString("nickname", "")!!,
            prefs.getString("studentId", "")!!.toInt(),
            prefs.getString("major", "")!!,
            prefs.getString("gender", "")!!,
            prefs.getInt("image", -1)
        )
    }

    fun removeAll() {
        prefs.edit().clear().apply()
    }
}