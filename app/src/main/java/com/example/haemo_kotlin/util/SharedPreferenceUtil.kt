package com.example.haemo_kotlin.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {
    private val PREF_NAME = "MyPrefs"
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, 0)

    companion object {
        const val KEY_UID = "UID"
    }

    var uid: String? = null
        get() = getString(KEY_UID,"")

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

}