package dev.sandroisu.common

import android.util.Log

public interface Logger {
    public fun debug(tag: String = "DEBUG_DEV", message: String)
    public fun error(tag: String = "ERROR_DEV", message: String)
}

public fun androidLogcatLogger(): Logger = object : Logger {
    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun error(tag: String, message: String) {
        Log.e(tag, message)
    }
}
