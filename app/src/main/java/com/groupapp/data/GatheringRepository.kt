package com.groupapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Single source of truth. Every mutation updates the in-memory state and
 * immediately persists it, so the app survives a restart.
 */
class GatheringRepository(private val store: LocalStore) {

    private val _data = MutableStateFlow(store.load() ?: DemoData.initial())
    val data: StateFlow<AppData> = _data.asStateFlow()

    private fun update(block: (AppData) -> AppData) {
        val next = block(_data.value)
        _data.value = next
        store.save(next)
    }

    fun setCity(city: String?) = update { it.copy(selectedCity = city) }

    fun toggleJoin(gatheringId: String) = update { current ->
        val me = current.currentUserId
        current.copy(
            gatherings = current.gatherings.map { g ->
                when {
                    g.id != gatheringId -> g
                    g.creatorId == me -> g                       // organiser is always in
                    g.participantIds.contains(me) -> g.copy(participantIds = g.participantIds - me)
                    g.isFull() -> g                              // no free seats
                    else -> g.copy(participantIds = g.participantIds + me)
                }
            }
        )
    }

    fun createGathering(
        title: String,
        description: String,
        category: Category,
        city: String,
        place: String,
        dateTime: Long,
        price: Double,
        discountPercent: Int,
        peopleNeeded: Int
    ): String {
        val id = "g" + System.currentTimeMillis()
        update { current ->
            val gathering = Gathering(
                id = id,
                title = title.trim(),
                description = description.trim(),
                category = category,
                city = city,
                place = place.trim(),
                dateTime = dateTime,
                price = price.coerceAtLeast(0.0),
                discountPercent = discountPercent.coerceIn(0, 100),
                peopleNeeded = peopleNeeded.coerceIn(1, 999),
                participantIds = listOf(current.currentUserId),
                creatorId = current.currentUserId,
                createdAt = System.currentTimeMillis()
            )
            current.copy(gatherings = current.gatherings + gathering)
        }
        return id
    }

    fun deleteGathering(gatheringId: String) = update { current ->
        current.copy(
            gatherings = current.gatherings.filterNot {
                it.id == gatheringId && it.creatorId == current.currentUserId
            }
        )
    }

    fun addReview(targetUserId: String, rating: Int, text: String) = update { current ->
        val author = current.users.firstOrNull { it.id == current.currentUserId }
        val review = Review(
            id = "r" + System.currentTimeMillis(),
            targetUserId = targetUserId,
            authorId = current.currentUserId,
            authorName = author?.name ?: "Вы",
            rating = rating.coerceIn(1, 5),
            text = text.trim(),
            createdAt = System.currentTimeMillis()
        )
        current.copy(reviews = current.reviews + review)
    }

    fun updateProfile(name: String, city: String, bio: String, emoji: String) = update { current ->
        current.copy(
            users = current.users.map { user ->
                if (user.id != current.currentUserId) user
                else user.copy(
                    name = name.trim().ifBlank { user.name },
                    city = city,
                    bio = bio.trim(),
                    emoji = emoji
                )
            }
        )
    }
}
