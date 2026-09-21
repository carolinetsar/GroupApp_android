package com.groupapp.data

import kotlinx.serialization.Serializable

/** The five gathering categories offered on the home screen. */
@Serializable
enum class Category(val label: String, val emoji: String) {
    CAFE("Кафе", "☕"),
    GROUP_PURCHASE("Совместная покупка", "🛒"),
    RENT("Аренда", "🏠"),
    WALK("Прогулка", "🌳"),
    OTHER("Другое", "✨");

    companion object {
        fun byName(value: String?): Category =
            entries.firstOrNull { it.name == value } ?: OTHER
    }
}
