package com.example.weirdweatherreports.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weirdweatherreports.data.dao.Weather
import kotlin.math.roundToInt

@Composable
fun TodayScreen(
    weather: Weather?,
    modifier: Modifier = Modifier
) {
    if (weather == null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val current = weather.current
    val precipChance = maxOf(current.chanceOfRain ?: 0, current.chanceOfSnow ?: 0)

    val gradient = if (current.isDay == 1) {
        listOf(Color(0xFF4FC3F7), Color(0xFF1976D2))
    } else {
        listOf(Color(0xFF283593), Color(0xFF0D1330))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradient))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            model = current.condition.iconUrl.replace("64x64", "128x128"),
            contentDescription = current.condition.text,
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = current.condition.text,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Text(
            text = "${current.tempC.roundToInt()}°",
            fontSize = 96.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )

        Text(
            text = "Feels like ${current.feelsLikeC.roundToInt()}°C",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoTile("Wind", "${current.windDir} ${current.windKph.roundToInt()} km/h", Modifier.weight(1f))
            InfoTile("Humidity", "${current.humidity}%", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoTile("Precip. chance", "$precipChance%", Modifier.weight(1f))
            InfoTile("Precip. amount", "${current.precipMm} mm", Modifier.weight(1f))
        }
    }
}

@Composable
private fun InfoTile(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}