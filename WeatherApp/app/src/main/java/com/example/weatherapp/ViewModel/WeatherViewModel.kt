package com.example.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.Repository.WeatherRepository
import com.example.weatherapp.Server.ApiClient
import com.example.weatherapp.Server.ApiServices

class WeatherViewModel(val repository: WeatherRepository): ViewModel() {
    constructor():this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat:Double,lng:Double,units:String)
            = repository.getCurrentWeather(lat,lng,units)

    fun loadForecastWeather(lat:Double,lng:Double,units:String)
            = repository.getForecastWeather(lat,lng,units)
}