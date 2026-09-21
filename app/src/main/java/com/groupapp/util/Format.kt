package com.groupapp.util

import java.util.Calendar
import java.util.Locale

private val MONTHS = arrayOf(
    "янв", "фев", "мар", "апр", "мая", "июн",
    "июл", "авг", "сен", "окт", "ноя", "дек"
)

private val WEEKDAYS = arrayOf("вс", "пн", "вт", "ср", "чт", "пт", "сб")

/** e.g. "24 сен, 19:00" */
fun formatDateTime(millis: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = millis }
    val day = c.get(Calendar.DAY_OF_MONTH)
    val month = MONTHS[c.get(Calendar.MONTH)]
    val hour = c.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    val minute = c.get(Calendar.MINUTE).toString().padStart(2, '0')
    return "$day $month, $hour:$minute"
}

/** e.g. "ср, 24 сен" */
fun formatDateShort(millis: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = millis }
    val weekday = WEEKDAYS[c.get(Calendar.DAY_OF_WEEK) - 1]
    return "$weekday, ${c.get(Calendar.DAY_OF_MONTH)} ${MONTHS[c.get(Calendar.MONTH)]}"
}

/** e.g. "24 сентября 2026" */
fun formatDateLong(millis: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = millis }
    val months = arrayOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    return "${c.get(Calendar.DAY_OF_MONTH)} ${months[c.get(Calendar.MONTH)]} ${c.get(Calendar.YEAR)}"
}

fun formatTime(millis: Long): String {
    val c = Calendar.getInstance().apply { timeInMillis = millis }
    return "${c.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')}:${
        c.get(Calendar.MINUTE).toString().padStart(2, '0')
    }"
}

fun formatPrice(price: Double): String {
    if (price <= 0.0) return "Бесплатно"
    return if (price % 1.0 == 0.0) "${price.toInt()} ₽"
    else String.format(Locale.US, "%.2f ₽", price)
}

fun formatRating(value: Double): String = String.format(Locale.US, "%.1f", value)

fun pluralReviews(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100
    val word = when {
        mod10 == 1 && mod100 != 11 -> "отзыв"
        mod10 in 2..4 && mod100 !in 12..14 -> "отзыва"
        else -> "отзывов"
    }
    return "$count $word"
}
