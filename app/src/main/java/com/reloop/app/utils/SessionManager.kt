package com.reloop.app.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("reloop_session", Context.MODE_PRIVATE)

    fun saveSession(userID: Int, role: String) {
        prefs.edit().putInt(KEY_USER_ID, userID).putString(KEY_ROLE, role).apply()
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getRole(): String = prefs.getString(KEY_ROLE, "") ?: ""
    fun isLoggedIn(): Boolean = getUserId() != -1

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ROLE = "role"
    }
}
