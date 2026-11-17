package com.example.weirdweatherreports.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.weirdweatherreports.MainViewModel
import com.example.weirdweatherreports.WeatherUiState
import com.example.weirdweatherreports.ui.screens.LocationsScreen
import com.example.weirdweatherreports.ui.screens.TodayScreen
import com.example.weirdweatherreports.ui.screens.WeeklyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(uiState: WeatherUiState, viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showLocations by remember { mutableStateOf(false) }

    BackHandler(enabled = showLocations) { showLocations = false }

    val location = uiState.weather?.location
    val title = when {
        showLocations -> "Locations"
        location != null -> "${location.name}, ${location.region}"
        else -> "Loading..."
    }

    val isDay = uiState.weather?.current?.isDay != 0

    val topBarColor = if (isDay) Color(0xFF4FC3F7) else Color(0xFF283593)
    val bottomBarColor = if (isDay) Color(0xFF1976D2) else Color(0xFF0D1330)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    if (showLocations) {
                        IconButton(onClick = { showLocations = false }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (!showLocations) {
                        IconButton(onClick = { showLocations = true }) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Locations")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!showLocations) {
                NavigationBar(containerColor = bottomBarColor) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = Color.White.copy(alpha = 0.25f),
                        unselectedIconColor = Color.White.copy(alpha = 0.7f),
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Today") },
                        label = { Text("Today") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Forecast") },
                        label = { Text("Forecast") },
                        colors = itemColors
                    )
                }
            }
        }
    ) { padding ->
        if (showLocations) {
            LocationsScreen(
                uiState = uiState,
                onSelect = { selected ->
                    viewModel.loadWeather(selected.lat, selected.lon)
                    showLocations = false
                },
                onDeviceLocation = { lat, lon -> viewModel.updateMyLocation(lat, lon) },
                onAdd = { viewModel.addLocation(it) },
                onDelete = { viewModel.removeLocation(it) },
                modifier = Modifier.padding(padding)
            )
        } else if (uiState.error != null && uiState.weather == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${uiState.error}")
            }
        } else if (selectedTab == 0) {
            TodayScreen(weather = uiState.weather, modifier = Modifier.padding(padding))
        } else {
            WeeklyScreen(weather = uiState.weather, modifier = Modifier.padding(padding))
        }
    }
}