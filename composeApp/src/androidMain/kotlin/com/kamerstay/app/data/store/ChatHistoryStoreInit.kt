package com.kamerstay.app.data.store

import android.content.Context

fun initChatHistoryStore(context: Context) {
    val prefs = context.getSharedPreferences("kamsa_chat", Context.MODE_PRIVATE)
    ChatHistoryStore.init(
        saver = { json -> prefs.edit().putString("history", json).apply() },
        loader = { prefs.getString("history", null)?.takeIf { it.isNotBlank() } }
    )
}
