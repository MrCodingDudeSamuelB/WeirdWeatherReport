package com.example.weirdweatherreports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weirdweatherreports.data.LocationStore
import com.example.weirdweatherreports.data.WeatherApi
import com.example.weirdweatherreports.data.dao.SavedLocation
import com.example.weirdweatherreports.data.dao.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val myLocation: SavedLocation? = null,
    val savedLocations: List<SavedLocation> = emptyList(),
    val message: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val store = LocationStore(application)

    private val _uiState = MutableStateFlow(WeatherUiState(savedLocations = store.load()))
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()


    fun loadWeather(lat: Double, lon: Double) {
        loadWeather("$lat,$lon")
    }

    fun loadWeather(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = WeatherApi.instance.getForecast(query = query)
                _uiState.update { it.copy(weather = result, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Could not load weather")
                }
            }
        }
    }

    fun onLocationUnavailable() {
        loadWeather(DEFAULT_CITY)
    }

    fun updateMyLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val loc = WeatherApi.instance.getForecast(query = "$lat,$lon", days = 1).location
                _uiState.update {
                    it.copy(myLocation = SavedLocation(loc.name, loc.region, loc.lat, loc.lon))
                }
            } catch (e: Exception) {

            }
        }
    }
    fun addLocation(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            try {
                val match = WeatherApi.instance
                    .searchLocations(query = query.trim())
                    .firstOrNull { it.country.equals("Canada", ignoreCase = true) }

                if (match == null) {
                    _uiState.update { it.copy(message = "No Canadian city found for \"${query.trim()}\"") }
                    return@launch
                }

                val newLocation = SavedLocation(match.name, match.region, match.lat, match.lon)
                val current = _uiState.value.savedLocations

                if (current.any { it.name == newLocation.name && it.region == newLocation.region }) {
                    _uiState.update { it.copy(message = "${newLocation.name} is already saved") }
                    return@launch
                }

                val updated = current + newLocation
                store.save(updated)
                _uiState.update {
                    it.copy(savedLocations = updated, message = "Added ${newLocation.name}, ${newLocation.region}")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(message = "Search failed: ${e.message}") }
            }
        }
    }

    fun removeLocation(location: SavedLocation) {
        val updated = _uiState.value.savedLocations - location
        store.save(updated)
        _uiState.update { it.copy(savedLocations = updated) }
    }

    companion object {
        private const val DEFAULT_CITY = "Halifax"
    }
}