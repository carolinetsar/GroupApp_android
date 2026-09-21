package com.groupapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.groupapp.data.Category
import com.groupapp.data.ratingOf
import com.groupapp.data.userById
import com.groupapp.ui.AppBottomBar
import com.groupapp.ui.AppViewModel
import com.groupapp.ui.Routes
import com.groupapp.ui.components.CityPickerDialog
import com.groupapp.ui.components.EmptyState
import com.groupapp.ui.components.GatheringCard
import com.groupapp.ui.components.Pill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(vm: AppViewModel, nav: NavHostController, goTab: (String) -> Unit) {
    val data by vm.data.collectAsState()
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf<String?>(null) }
    var showCityPicker by remember { mutableStateOf(false) }

    val category = categoryName?.let { Category.byName(it) }

    val list = remember(data.gatherings, data.selectedCity, category, query) {
        val q = query.trim()
        data.gatherings
            .filter { data.selectedCity == null || it.city == data.selectedCity }
            .filter { category == null || it.category == category }
            .filter {
                q.isBlank() ||
                        it.title.contains(q, ignoreCase = true) ||
                        it.place.contains(q, ignoreCase = true) ||
                        it.description.contains(q, ignoreCase = true)
            }
            .sortedBy { it.dateTime }
    }

    val onCityResolved: (String?) -> Unit = { city ->
        if (city == null) {
            Toast.makeText(context, "Не удалось определить город", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) vm.detectCity(onCityResolved)
        else Toast.makeText(context, "Нет доступа к геолокации", Toast.LENGTH_SHORT).show()
    }

    val detectCity: () -> Unit = {
        val allowed = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (allowed) vm.detectCity(onCityResolved)
        else permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    if (showCityPicker) {
        CityPickerDialog(
            current = data.selectedCity,
            onDismiss = { showCityPicker = false },
            onPick = {
                vm.setCity(it)
                showCityPicker = false
            },
            onDetect = {
                showCityPicker = false
                detectCity()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сборы") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showCityPicker = true }) {
                        Icon(Icons.Default.Place, contentDescription = "Город")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate(Routes.CREATE) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Создать сбор") }
            )
        },
        bottomBar = { AppBottomBar(Routes.FEED, goTab) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Pill(
                    text = data.selectedCity ?: "Все города",
                    onClick = { showCityPicker = true },
                    leading = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${list.size} сборов",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Поиск по названию или месту") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            LazyRow(
                modifier = Modifier.padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = category == null,
                        onClick = { categoryName = null },
                        label = { Text("Все") }
                    )
                }
                items(Category.entries.toList(), key = { it.name }) { item ->
                    FilterChip(
                        selected = category == item,
                        onClick = { categoryName = item.name },
                        label = { Text("${item.emoji} ${item.label}") }
                    )
                }
            }

            if (list.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    EmptyState(
                        emoji = "🔍",
                        title = "Здесь пока пусто",
                        subtitle = "Создайте свой сбор или выберите другой город"
                    )
                    Button(onClick = { nav.navigate(Routes.CREATE) }) {
                        Text("Создать сбор")
                    }
                    Spacer(Modifier.size(72.dp))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 2.dp, bottom = 96.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(list, key = { it.id }) { gathering ->
                        GatheringCard(
                            gathering = gathering,
                            creator = data.userById(gathering.creatorId),
                            creatorRating = data.ratingOf(gathering.creatorId),
                            isJoined = gathering.participantIds.contains(data.currentUserId),
                            isMine = gathering.creatorId == data.currentUserId,
                            onClick = { nav.navigate(Routes.detail(gathering.id)) }
                        )
                    }
                }
            }
        }
    }
}
