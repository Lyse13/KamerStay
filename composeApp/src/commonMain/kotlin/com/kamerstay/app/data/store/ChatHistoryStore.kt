package com.kamerstay.app.data.store

object ChatHistoryStore {
    private var saver: ((String) -> Unit)? = null
    private var loader: (() -> String?)? = null

    fun init(saver: (String) -> Unit, loader: () -> String?) {
        this.saver = saver
        this.loader = loader
    }

    fun save(json: String) = saver?.invoke(json)
    fun load(): String? = loader?.invoke()
    fun clear() = saver?.invoke("")
}
