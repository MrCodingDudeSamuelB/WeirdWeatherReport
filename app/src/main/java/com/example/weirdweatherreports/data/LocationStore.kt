package com.example.weirdweatherreports.data

import android.content.Context
import com.example.weirdweatherreports.data.dao.SavedLocation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationStore(context: Context) {

    private val prefs = context.getSharedPreferences("saved_locations", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun load(): List<SavedLocation> {
        val json = prefs.getString("list", null) ?: return emptyList()
        val type = object : TypeToken<List<SavedLocation>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun save(list: List<SavedLocation>) {
        prefs.edit().putString("list", gson.toJson(list)).apply()
    }
}