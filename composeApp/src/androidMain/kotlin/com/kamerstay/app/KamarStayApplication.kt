package com.kamerstay.app

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kamerstay.app.data.remote.NotificationRemoteRepository
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.data.store.initChatHistoryStore
import com.kamerstay.app.data.store.initSessionStore
import com.kamerstay.app.di.initKoin
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KamarStayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserSession.language = if (Locale.getDefault().language == "en") "en" else "fr"
        initKoin()
        initSessionStore(this)
        initChatHistoryStore(this)
        // Initialiser Firebase et enregistrer le token FCM
        try {
            FirebaseApp.initializeApp(this)
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                if (UserSession.token.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try { NotificationRemoteRepository().registerFcmToken(token) }
                        catch (_: Exception) {}
                    }
                }
            }
        } catch (_: Exception) {
            // Firebase non configuré (google-services.json manquant) → ignorer
        }
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory(HttpClient(Android)))
                }
                .build()
        }
    }
}