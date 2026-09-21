package com.groupapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.groupapp.data.isFull
import com.groupapp.data.ratingOf
import com.groupapp.data.reviewsFor
import com.groupapp.data.userById
import com.groupapp.ui.AppViewModel
import com.groupapp.ui.components.Avatar
import com.groupapp.ui.components.InfoRow
import com.groupapp.ui.components.ProgressBar
import com.groupapp.ui.components.RatingStars
import com.groupapp.ui.components.ReviewDialog
import com.groupapp.ui.components.ReviewItem
import com.groupapp.ui.components.SectionTitle
import com.groupapp.ui.components.Tag
import com.groupapp.util.formatDateLong
import com.groupapp.util.formatPrice
import com.groupapp.util.formatRating
import com.groupapp.util.formatTime
import com.groupapp.util.pluralReviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: AppViewModel, nav: NavHostController, gatheringId: String) {
    val data by vm.data.collectAsState()
    val context = LocalContext.current

    var showReview by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }

    val gathering = data.gatherings.firstOrNull { it.id == gatheringId }

    if (gathering == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Сбор") },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Сбор не найден",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    val me = data.currentUserId
    val isMine = gathering.creatorId == me
    val isJoined = gathering.participantIds.contains(me)
    val full = gathering.isFull()
    val creator = data.userById(gathering.creatorId)
    val creatorBio = creator?.bio.orEmpty()
    val creatorRating = data.ratingOf(gathering.creatorId)
    val creatorReviews = data.reviewsFor(gathering.creatorId)

    if (showReview) {
        ReviewDialog(
            targetName = creator?.name ?: "организаторе",
            onDismiss = { showReview = false },
            onSubmit = { rating, text ->
                vm.addReview(gathering.creatorId, rating, text)
                showReview = false
                Toast.makeText(context, "Спасибо за отзыв!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Удалить сбор?") },
            text = { Text("«${gathering.title}» будет удалён без возможности восстановления.") },
            confirmButton = {
                TextButton(onClick = {
                    showDelete = false
                    vm.deleteGathering(gathering.id)
                    Toast.makeText(context, "Сбор удалён", Toast.LENGTH_SHORT).show()
                    nav.popBackStack()
                }) { Text("Удалить") }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false }) { Text("Отмена") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gathering.category.label) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (isMine) {
                        IconButton(onClick = { showDelete = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { vm.toggleJoin(gathering.id) },
                        enabled = isMine || isJoined || !full,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text = when {
                                isMine -> "Вы организатор"
                                isJoined -> "Выйти из сбора"
                                full -> "Мест больше нет"
                                else -> "Присоединиться"
                            },
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = gathering.category.emoji, fontSize = 28.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gathering.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Tag(gathering.category.label)
                        if (gathering.discountPercent > 0) {
                            Tag("-${gathering.discountPercent}%")
                        }
                        if (isMine) Tag("Ваш сбор")
                    }
                }
            }

            if (gathering.description.isNotBlank()) {
                Spacer(Modifier.height(14.dp))
                Text(text = gathering.description, style = MaterialTheme.typography.bodyLarge)
            }

            SectionTitle("Детали")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    InfoRow("📍", "Место", gathering.place)
                    InfoRow(
                        "🕒",
                        "Когда",
                        "${formatDateLong(gathering.dateTime)}, ${formatTime(gathering.dateTime)}"
                    )
                    InfoRow("💰", "Цена с человека", formatPrice(gathering.price))
                    if (gathering.discountPercent > 0) {
                        InfoRow("🏷️", "Скидка", "${gathering.discountPercent}%")
                    }
                    InfoRow("👥", "Нужно людей", "${gathering.peopleNeeded}")
                    InfoRow("🏙️", "Город", gathering.city)
                }
            }

            SectionTitle("Участники")
            ProgressBar(
                progress = gathering.participantIds.size.toFloat() /
                        gathering.peopleNeeded.coerceAtLeast(1)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${gathering.participantIds.size} из ${gathering.peopleNeeded} · " +
                        if (full) "мест больше нет" else "свободно ${gathering.peopleNeeded - gathering.participantIds.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                gathering.participantIds.forEach { participantId ->
                    val user = data.userById(participantId)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Avatar(emoji = user?.emoji ?: "🙂", size = 44.dp)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = user?.name ?: "Гость",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            SectionTitle("Организатор")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (!isMine) showReview = true
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(emoji = creator?.emoji ?: "🙂", size = 48.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = creator?.name ?: "Организатор",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (!creator?.bio.isNullOrBlank()) {
                            Text(
                                text = creator?.bio ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        RatingStars(
                            rating = creatorRating.value,
                            showValue = creatorRating.count > 0,
                            count = creatorRating.count
                        )
                    }
                }
            }

            SectionTitle("Отзывы об организаторе")
            if (creatorReviews.isEmpty()) {
                Text(
                    text = "Пока нет отзывов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "${formatRating(creatorRating.value)} из 5 · " +
                            pluralReviews(creatorRating.count),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                creatorReviews.take(5).forEach { review ->
                    ReviewItem(review = review, author = data.userById(review.authorId))
                }
            }

            Spacer(Modifier.height(10.dp))
            if (!isMine) {
                OutlinedButton(
                    onClick = { showReview = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Оставить отзыв")
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}
