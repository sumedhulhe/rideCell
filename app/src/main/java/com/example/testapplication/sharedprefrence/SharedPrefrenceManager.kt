package com.example.testapplication.sharedprefrence
import android.app.Activity
import com.example.testapplication.baseclasses.BaseApplication.Companion.getInstance
import android.content.SharedPreferences

class SharedPrefrenceManager {
    companion object {
        private var preferences: SharedPreferences? = null
        private const val APP_ID = "shared_pref"
        private const val WORLD_READABLE = Activity.MODE_PRIVATE
        const val TOKEN = "TOKEN"
        const val USER_KEY = "USER_KEY"

    fun readString(key: String): String? {
        return getSharedPrefInstance()?.getString(key, "")
    }

    fun writeString(key: String, value: String) {
        val editor = getSharedPrefInstance()?.edit()
        editor?.remove(key)
        editor?.apply()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getSharedPrefInstance(): SharedPreferences? {
        preferences?.let {
            return preferences
        }
        preferences = getInstance().getSharedPreferences(APP_ID, WORLD_READABLE)
        return preferences
    }
    }
}