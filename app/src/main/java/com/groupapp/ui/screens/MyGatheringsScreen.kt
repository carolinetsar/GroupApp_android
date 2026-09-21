package com.groupapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.groupapp.data.myCreated
import com.groupapp.data.myJoined
import com.groupapp.data.ratingOf
import com.groupapp.data.userById
import com.groupapp.ui.AppBottomBar
import com.groupapp.ui.AppViewModel
import com.groupapp.ui.Routes
import com.groupapp.ui.components.EmptyState
import com.groupapp.ui.components.GatheringCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGatheringsScreen(vm: AppViewModel, nav: NavHostController, goTab: (String) -> Unit) {
    val data by vm.data.collectAsState()
    var tab by remember { mutableStateOf(0) }

    val created = data.myCreated()
    val joined = data.myJoined()
    val list = if (tab == 0) created else joined

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои сборы") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate(Routes.CREATE) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Создать") }
            )
        },
        bottomBar = { AppBottomBar(Routes.MY, goTab) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = { Text("Созданные (${created.size})") }
                )
                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = { Text("Участвую (${joined.size})") }
                )
            }

            if (list.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    EmptyState(
                        emoji = if (tab == 0) "📣" else "🙋",
                        title = if (tab == 0) "Вы ещё не создавали сборы"
                        else "Вы пока ни к чему не присоединились",
                        subtitle = if (tab == 0) "Нажмите «Создать», чтобы собрать людей"
                        else "Найдите интересный сбор на главной"
                    )
                    Spacer(Modifier.size(72.dp))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp
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
