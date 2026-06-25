package com.kamerstay.app

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.kamerstay.app.data.store.initChatHistoryStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

class KamarStayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initChatHistoryStore(this)
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory(HttpClient(Android)))
                }
                .build()
        }
    }
}