package com.example.weirdweatherreports.data

import com.example.weirdweatherreports.BuildConfig
import com.example.weirdweatherreports.data.dao.Weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.weirdweatherreports.data.dao.SearchResult

interface WeatherApi {

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("days") days: Int = 3,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"

    ): Weather
    @GET("search.json")
    suspend fun searchLocations(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String
    ): List<SearchResult>

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY: String = BuildConfig.API_KEY

        val instance: WeatherApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}