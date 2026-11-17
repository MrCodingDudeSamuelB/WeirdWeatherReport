package com.example.weirdweatherreports.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weirdweatherreports.WeatherUiState
import com.example.weirdweatherreports.data.dao.SavedLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun LocationsScreen(
    uiState: WeatherUiState,
    onSelect: (SavedLocation) -> Unit,
    onDeviceLocation: (Double, Double) -> Unit,
    onAdd: (String) -> Unit,
    onDelete: (SavedLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    // Asks for location permission (no prompt if it was already granted)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.any { it }) {
            fetchDeviceLocation(context, onDeviceLocation)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ----- My Location -----
        item { SectionHeader("My Location") }
        item {
            val mine = uiState.myLocation
            if (mine != null) {
                LocationCard(location = mine, onClick = { onSelect(mine) })
            } else {
                Text("Not available (location permission needed)")
            }
        }

        // ----- Search / add -----
        item { SectionHeader("Add a Canadian city") }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("City name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    onAdd(query)
                    query = ""
                }) {
                    Text("Add")
                }
            }
        }
        uiState.message?.let { message ->
            item { Text(message, style = MaterialTheme.typography.bodyMedium) }
        }

        // ----- Saved Locations -----
        item { SectionHeader("Saved Locations") }
        if (uiState.savedLocations.isEmpty()) {
            item { Text("No saved locations yet") }
        }
        items(uiState.savedLocations) { saved ->
            LocationCard(
                location = saved,
                onClick = { onSelect(saved) },
                onDelete = { onDelete(saved) }
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun LocationCard(
    location: SavedLocation,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(location.name, style = MaterialTheme.typography.titleMedium)
                Text(location.region, style = MaterialTheme.typography.bodyMedium)
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove ${location.name}")
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchDeviceLocation(context: Context, onFound: (Double, Double) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context)
        .getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        )
        .addOnSuccessListener { location ->
            if (location != null) onFound(location.latitude, location.longitude)
        }
}