package com.groupapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groupapp.data.Gathering
import com.groupapp.data.Rating
import com.groupapp.data.User
import com.groupapp.data.freeSeats
import com.groupapp.data.isFull
import com.groupapp.ui.theme.AmberSoft
import com.groupapp.ui.theme.MintSoft
import com.groupapp.util.formatDateTime
import com.groupapp.util.formatPrice
import com.groupapp.util.formatRating

private val AmberText = Color(0xFF6B4400)
private val MintText = Color(0xFF00513F)

@Composable
fun GatheringCard(
    gathering: Gathering,
    creator: User?,
    creatorRating: Rating,
    isJoined: Boolean,
    isMine: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = gathering.category.emoji, fontSize = 22.sp)
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gathering.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${gathering.category.label} · ${gathering.city}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                if (gathering.isFull()) {
                    Tag("Полный", background = AmberSoft, contentColor = AmberText)
                } else {
                    Tag("${gathering.freeSeats()} мест", background = MintSoft, contentColor = MintText)
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "📍 ${gathering.place}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "🕒 ${formatDateTime(gathering.dateTime)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatPrice(gathering.price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                if (gathering.discountPercent > 0) {
                    Spacer(Modifier.width(8.dp))
                    Tag("-${gathering.discountPercent}%", background = AmberSoft, contentColor = AmberText)
                }

                Spacer(Modifier.weight(1f))

                if (isMine) {
                    Tag("Ваш сбор")
                } else if (isJoined) {
                    Tag("Вы участвуете")
                }
            }

            Spacer(Modifier.height(10.dp))

            ProgressBar(
                progress = gathering.participantIds.size.toFloat() /
                        gathering.peopleNeeded.coerceAtLeast(1)
            )

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${gathering.participantIds.size} из ${gathering.peopleNeeded} участников",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                if (creator != null) {
                    Text(
                        text = "${creator.emoji} ${creator.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (creatorRating.count > 0) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "★ ${formatRating(creatorRating.value)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
