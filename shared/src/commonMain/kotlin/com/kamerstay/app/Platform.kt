package com.kamerstay.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform