package dev.sandroisu.common

import android.util.Log

interface Logger {
    fun debug(tag: String = "DEBUG_DEV", message: String)
}

fun AndroidLogcatLogger() = object : Logger{
    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }
}