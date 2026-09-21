package com.groupapp.data

import android.content.Context
import kotlinx.serialization.json.Json

/** Persists the whole app state as JSON in SharedPreferences. */
class LocalStore(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    fun load(): AppData? {
        val raw = prefs.getString(KEY_DATA, null) ?: return null
        return runCatching { json.decodeFromString(AppData.serializer(), raw) }.getOrNull()
    }

    fun save(data: AppData) {
        runCatching {
            prefs.edit().putString(KEY_DATA, json.encodeToString(AppData.serializer(), data)).apply()
        }
    }

    private companion object {
        const val PREFS_NAME = "groupapp_store"
        const val KEY_DATA = "app_data_v1"
    }
}
