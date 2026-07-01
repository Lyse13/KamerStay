package com.kamerstay.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kamerstay.app.MainActivity
import com.kamerstay.app.data.remote.ApiClient
import com.kamerstay.app.data.state.UserSession
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KamerStayFirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: "KamerStay"
        val body  = message.notification?.body  ?: ""
        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        // Envoyer le nouveau token au serveur si l'utilisateur est connecté
        if (UserSession.token.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    ApiClient.client.put("${ApiClient.BASE_URL}/auth/fcm-token") {
                        contentType(ContentType.Application.Json)
                        headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
                        setBody(mapOf("token" to token))
                    }
                } catch (_: Exception) {}
            }
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "kamerstay_bookings"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Créer le canal (Android 8+)
        val channel = NotificationChannel(
            channelId,
            "Réservations KamerStay",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Mises à jour de vos réservations" }
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
