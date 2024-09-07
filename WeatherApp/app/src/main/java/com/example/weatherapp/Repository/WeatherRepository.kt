package com.example.weatherapp.Repository

import com.example.weatherapp.Server.ApiServices

class WeatherRepository(
    val api: ApiServices
) {
    fun getCurrentWeather(
        lat: Double,
        lng: Double,
        units: String
    ) = api.getCurrentWeather(lat, lng, units, "3bd707a9fd7103fc174da48c8f3b7f02")

    fun getForecastWeather(
        lat: Double,
        lng: Double,
        units: String
    ) = api.getForecastWeather(lat, lng, units, "3bd707a9fd7103fc174da48c8f3b7f02")
}