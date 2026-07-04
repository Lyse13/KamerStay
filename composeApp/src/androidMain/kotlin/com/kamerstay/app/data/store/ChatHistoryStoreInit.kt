package com.kamerstay.app.data.store

import android.content.Context
import com.kamerstay.app.data.state.UserSession

fun initChatHistoryStore(context: Context) {
    val prefs = context.getSharedPreferences("kamsa_chat", Context.MODE_PRIVATE)
    ChatHistoryStore.init(
        saver = { json ->
            val key = "history_${UserSession.email.ifBlank { "anonymous" }}"
            prefs.edit().putString(key, json).apply()
        },
        loader = {
            val key = "history_${UserSession.email.ifBlank { "anonymous" }}"
            prefs.getString(key, null)?.takeIf { it.isNotBlank() }
        }
    )
}
