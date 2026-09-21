package com.groupapp.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val city: String = "",
    val bio: String = "",
    val emoji: String = "🙂"
)

@Serializable
data class Gathering(
    val id: String,
    val title: String,
    val description: String = "",
    val category: Category = Category.OTHER,
    val city: String = "",
    val place: String = "",
    /** Epoch millis of the meeting. */
    val dateTime: Long = 0L,
    /** Price per person in rubles. 0 means free. */
    val price: Double = 0.0,
    /** Discount in percent, 0..100. */
    val discountPercent: Int = 0,
    /** How many people the organiser needs in total. */
    val peopleNeeded: Int = 2,
    val participantIds: List<String> = emptyList(),
    val creatorId: String,
    val createdAt: Long = 0L
)

@Serializable
data class Review(
    val id: String,
    val targetUserId: String,
    val authorId: String,
    val authorName: String,
    val rating: Int,
    val text: String = "",
    val createdAt: Long = 0L
)

@Serializable
data class AppData(
    val users: List<User> = emptyList(),
    val gatherings: List<Gathering> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val currentUserId: String = "me",
    /** null means "all cities". */
    val selectedCity: String? = "Москва"
)

/** Aggregated rating of a user. */
data class Rating(val value: Double, val count: Int)

fun AppData.userById(id: String): User? = users.firstOrNull { it.id == id }

fun AppData.currentUser(): User? = userById(currentUserId)

fun AppData.ratingOf(userId: String): Rating {
    val mine = reviews.filter { it.targetUserId == userId }
    return if (mine.isEmpty()) Rating(0.0, 0) else Rating(mine.map { it.rating }.average(), mine.size)
}

fun AppData.reviewsFor(userId: String): List<Review> =
    reviews.filter { it.targetUserId == userId }.sortedByDescending { it.createdAt }

/** Gatherings created by the current user. */
fun AppData.myCreated(): List<Gathering> =
    gatherings.filter { it.creatorId == currentUserId }.sortedBy { it.dateTime }

/** Gatherings the current user joined (but did not create). */
fun AppData.myJoined(): List<Gathering> =
    gatherings.filter { it.creatorId != currentUserId && it.participantIds.contains(currentUserId) }
        .sortedBy { it.dateTime }

fun Gathering.isFull(): Boolean = participantIds.size >= peopleNeeded

fun Gathering.freeSeats(): Int = (peopleNeeded - participantIds.size).coerceAtLeast(0)
