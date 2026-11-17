package com.example.weirdweatherreports.data.dao

data class SearchResult(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

data class SavedLocation(
    val name: String,
    val region: String,
    val lat: Double,
    val lon: Double
)