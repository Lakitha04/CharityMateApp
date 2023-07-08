package com.example.charitymate

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_LOGGED_IN = "isLoggedIn"
        private const val KEY_PROFILE_NAME = "name"
        private const val KEY_PROFILE_EMAIL = "email"
        private const val KEY_PROFILE_USERNAME = "username"
        private const val KEY_PROFILE_CONTACT = "contact"
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun setProfileDetails(name: String, email: String, username: String, contact: String) {
        editor.putString(KEY_PROFILE_NAME, name)
        editor.putString(KEY_PROFILE_EMAIL, email)
        editor.putString(KEY_PROFILE_USERNAME, username)
        editor.putString(KEY_PROFILE_CONTACT, contact)
        editor.apply()
    }

    fun getProfileName(): String {
        return sharedPreferences.getString(KEY_PROFILE_NAME, "") ?: ""
    }

    fun getProfileEmail(): String {
        return sharedPreferences.getString(KEY_PROFILE_EMAIL, "") ?: ""
    }

    fun getProfileUsername(): String {
        return sharedPreferences.getString(KEY_PROFILE_USERNAME, "") ?: ""
    }

    fun getProfileContact(): String {
        return sharedPreferences.getString(KEY_PROFILE_CONTACT, "") ?: ""
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}