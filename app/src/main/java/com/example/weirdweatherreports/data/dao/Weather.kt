package com.example.weirdweatherreports.data.dao

import com.google.gson.annotations.SerializedName

data class Weather(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id") val tzId: String,
    val localtime: String
)

data class Current(
    @SerializedName("is_day") val isDay: Int = 1,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    val condition: Condition,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    val humidity: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    // Not in every API response, so nullable
    @SerializedName("chance_of_rain") val chanceOfRain: Int? = null,
    @SerializedName("chance_of_snow") val chanceOfSnow: Int? = null
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
) {
    val iconUrl: String
        get() = if (icon.startsWith("//")) "https:$icon" else icon
}

data class Forecast(
    @SerializedName("forecastday") val forecastDay: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("maxtemp_f") val maxTempF: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("mintemp_f") val minTempF: Double,
    @SerializedName("maxwind_kph") val maxWindKph: Double,
    @SerializedName("maxwind_mph") val maxWindMph: Double,
    @SerializedName("totalprecip_mm") val totalPrecipMm: Double,
    @SerializedName("totalprecip_in") val totalPrecipIn: Double,
    @SerializedName("avghumidity") val avgHumidity: Double,
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int,
    @SerializedName("daily_chance_of_snow") val chanceOfSnow: Int,
    val condition: Condition
)