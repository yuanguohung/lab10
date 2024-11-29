package com.example.weatherapp

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)
