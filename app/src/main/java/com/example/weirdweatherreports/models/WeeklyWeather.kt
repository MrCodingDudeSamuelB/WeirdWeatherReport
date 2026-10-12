package com.example.weirdweatherreports.models

data class WeeklyWeather(
    val name: String,
    val resourceId: Int,
    val temperature: String = "",
    val feelsLike: String = "",
)
