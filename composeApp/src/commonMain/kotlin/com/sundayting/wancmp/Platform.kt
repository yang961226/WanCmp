package com.sundayting.wancmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform