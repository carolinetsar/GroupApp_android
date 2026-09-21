package com.groupapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.groupapp.GroupApp
import com.groupapp.data.AppData
import com.groupapp.data.Category
import com.groupapp.data.GatheringRepository
import com.groupapp.data.Geo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: GatheringRepository = (app as GroupApp).repo

    val data: StateFlow<AppData> = repo.data

    fun setCity(city: String?) = repo.setCity(city)

    fun toggleJoin(gatheringId: String) = repo.toggleJoin(gatheringId)

    fun deleteGathering(gatheringId: String) = repo.deleteGathering(gatheringId)

    fun addReview(targetUserId: String, rating: Int, text: String) =
        repo.addReview(targetUserId, rating, text)

    fun updateProfile(name: String, city: String, bio: String, emoji: String) =
        repo.updateProfile(name, city, bio, emoji)

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
    ): String = repo.createGathering(
        title, description, category, city, place, dateTime, price, discountPercent, peopleNeeded
    )

    /** Resolves the nearest known city from the last location fix. Result is null on failure. */
    fun detectCity(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val city = withContext(Dispatchers.IO) {
                runCatching { Geo.nearestCity(getApplication()) }.getOrNull()
            }
            if (city != null) repo.setCity(city)
            onResult(city)
        }
    }
}
