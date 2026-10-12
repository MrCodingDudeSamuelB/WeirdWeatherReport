package com.example.weirdweatherreports.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weirdweatherreports.R
import com.example.weirdweatherreports.models.WeeklyWeather

@Composable
fun WeeklyScreen() {
    val weatherduringweek = listOf(
        WeeklyWeather(
            name = "Monday",
            resourceId = R.drawable.sun,
            temperature = "Temperature: 15°C",
            feelsLike = "Feels like: 14°C"
        ),
        WeeklyWeather(
            name = "Tuesday",
            resourceId = R.drawable.cloudy,
            temperature = "Temperature: 17°C",
            feelsLike = "Feels like: 13°C"
        ),
        WeeklyWeather(
            name = "Wednesday",
            resourceId = R.drawable.partial_rain,
            temperature = "Temperature: 16°C",
            feelsLike = "Feels like: 15°C"
        ),
        WeeklyWeather(
            name = "Thursday",
            resourceId = R.drawable.cloudy,
            temperature = "Temperature: 13°C",
            feelsLike = "Feels like: 14°C"
        ),
        WeeklyWeather(
            name = "Friday",
            resourceId = R.drawable.cloudy,
            temperature = "Temperature: 14°C",
            feelsLike = "Feels like: 14°C"
        ),
        WeeklyWeather(
            name = "Saturday",
            resourceId = R.drawable.sun,
            temperature = "Temperature: 24°C",
            feelsLike = "Feels like: 28°C"
        ),
        WeeklyWeather(
            name = "Sunday",
            resourceId = R.drawable.rainy_day,
            temperature = "Temperature: 18°C",
            feelsLike = "Feels like: 16°C"
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(weatherduringweek) { weatherweek ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = weatherweek.resourceId),
                        contentDescription = weatherweek.name,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = weatherweek.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (weatherweek.temperature.isNotEmpty()) {
                            Text(
                                text = weatherweek.temperature,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (weatherweek.feelsLike.isNotEmpty()) {
                            Text(
                                text = weatherweek.feelsLike,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
