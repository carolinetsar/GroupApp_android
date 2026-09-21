package com.groupapp.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.groupapp.data.CITY_NAMES
import com.groupapp.data.Category
import com.groupapp.ui.AppViewModel
import com.groupapp.ui.components.CityPickerDialog
import com.groupapp.ui.components.Pill
import com.groupapp.util.formatDateLong
import com.groupapp.util.formatPrice
import com.groupapp.util.formatTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGatheringScreen(vm: AppViewModel, nav: NavHostController) {
    val data by vm.data.collectAsState()
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(Category.CAFE) }
    var city by remember { mutableStateOf(data.selectedCity ?: CITY_NAMES.first()) }
    var place by remember { mutableStateOf("") }
    var dateTime by remember {
        mutableStateOf(System.currentTimeMillis() + 24L * 60L * 60L * 1000L)
    }
    var price by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var people by remember { mutableStateOf(4) }
    var showErrors by remember { mutableStateOf(false) }
    var showCityPicker by remember { mutableStateOf(false) }

    val openDatePicker: () -> Unit = {
        val calendar = Calendar.getInstance().apply { timeInMillis = dateTime }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val updated = Calendar.getInstance().apply {
                    timeInMillis = dateTime
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                dateTime = updated.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val openTimePicker: () -> Unit = {
        val calendar = Calendar.getInstance().apply { timeInMillis = dateTime }
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val updated = Calendar.getInstance().apply {
                    timeInMillis = dateTime
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                dateTime = updated.timeInMillis
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    val priceValue = price.replace(',', '.').toDoubleOrNull() ?: 0.0
    val discountValue = (discount.toIntOrNull() ?: 0).coerceIn(0, 100)

    if (showCityPicker) {
        CityPickerDialog(
            current = city,
            onDismiss = { showCityPicker = false },
            onPick = { picked ->
                if (picked != null) city = picked
                showCityPicker = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый сбор") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название") },
                placeholder = { Text("Например: кофе на Патриарших") },
                isError = showErrors && title.isBlank(),
                supportingText = {
                    if (showErrors && title.isBlank()) Text("Введите название сбора")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                placeholder = { Text("Что будете делать, кого ищете") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Категория",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Category.entries.forEach { item ->
                    FilterChip(
                        selected = category == item,
                        onClick = { category = item },
                        label = { Text("${item.emoji} ${item.label}") }
                    )
                }
            }

            Text(
                text = "Город",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
            )
            Pill(
                text = city,
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

            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = place,
                onValueChange = { place = it },
                label = { Text("Место") },
                placeholder = { Text("Адрес или ориентир") },
                isError = showErrors && place.isBlank(),
                supportingText = {
                    if (showErrors && place.isBlank()) Text("Укажите место встречи")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Когда",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
            )
            OutlinedButton(onClick = openDatePicker, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(formatDateLong(dateTime))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = openTimePicker, modifier = Modifier.fillMaxWidth()) {
                Text("Начало в ${formatTime(dateTime)}")
            }

            Text(
                text = "Цена и скидка",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { input ->
                        price = input.filter { it.isDigit() || it == ',' || it == '.' }.take(9).toString()
                    },
                    label = { Text("Цена, ₽") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = discount,
                    onValueChange = { input ->
                        discount = input.filter { it.isDigit() }.take(3).toString()
                    },
                    label = { Text("Скидка, %") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Сколько человек нужно",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { if (people > 1) people-- },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(46.dp)
                ) {
                    Text("−", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    text = people.toString(),
                    modifier = Modifier.width(64.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedButton(
                    onClick = { if (people < 99) people++ },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(46.dp)
                ) {
                    Text("+", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Итого", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Нужно участников: $people",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Цена с человека: ${formatPrice(priceValue)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (discountValue > 0) {
                        Text(
                            text = "Скидка: $discountValue%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "${category.emoji} ${category.label} · $city",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    showErrors = true
                    if (title.isNotBlank() && place.isNotBlank()) {
                        vm.createGathering(
                            title = title,
                            description = description,
                            category = category,
                            city = city,
                            place = place,
                            dateTime = dateTime,
                            price = priceValue,
                            discountPercent = discountValue,
                            peopleNeeded = people
                        )
                        Toast.makeText(context, "Сбор создан", Toast.LENGTH_SHORT).show()
                        nav.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Создать сбор", style = MaterialTheme.typography.titleSmall)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
