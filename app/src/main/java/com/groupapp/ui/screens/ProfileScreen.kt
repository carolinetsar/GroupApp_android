package com.groupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.groupapp.data.currentUser
import com.groupapp.data.myCreated
import com.groupapp.data.myJoined
import com.groupapp.data.ratingOf
import com.groupapp.data.reviewsFor
import com.groupapp.data.userById
import com.groupapp.ui.AppBottomBar
import com.groupapp.ui.AppViewModel
import com.groupapp.ui.Routes
import com.groupapp.ui.components.Avatar
import com.groupapp.ui.components.EditProfileDialog
import com.groupapp.ui.components.RatingStars
import com.groupapp.ui.components.ReviewItem
import com.groupapp.ui.components.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: AppViewModel, nav: NavHostController, goTab: (String) -> Unit) {
    val data by vm.data.collectAsState()
    var showEdit by remember { mutableStateOf(false) }

    val user = data.currentUser()
    if (user == null) return

    val rating = data.ratingOf(user.id)
    val reviews = data.reviewsFor(user.id)
    val createdCount = data.myCreated().size
    val joinedCount = data.myJoined().size

    if (showEdit) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEdit = false },
            onSave = { name, city, bio, emoji ->
                vm.updateProfile(name, city, bio, emoji)
                showEdit = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = { AppBottomBar(Routes.PROFILE, goTab) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(emoji = user.emoji, size = 72.dp)
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (user.city.isBlank()) "Город не указан" else "📍 ${user.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    RatingStars(
                        rating = rating.value,
                        showValue = rating.count > 0,
                        count = rating.count
                    )
                }
            }

            if (user.bio.isNotBlank()) {
                Spacer(Modifier.height(14.dp))
                Text(text = user.bio, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showEdit = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Редактировать профиль")
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(label = "Создано", value = createdCount.toString(), modifier = Modifier.weight(1f))
                StatCard(label = "Участвую", value = joinedCount.toString(), modifier = Modifier.weight(1f))
                StatCard(label = "Отзывов", value = rating.count.toString(), modifier = Modifier.weight(1f))
            }

            SectionTitle("Отзывы обо мне")

            if (reviews.isEmpty()) {
                Text(
                    text = "Пока нет отзывов. Присоединяйтесь к сборам — организаторы смогут вас оценить.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                reviews.forEach { review ->
                    ReviewItem(review = review, author = data.userById(review.authorId))
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
