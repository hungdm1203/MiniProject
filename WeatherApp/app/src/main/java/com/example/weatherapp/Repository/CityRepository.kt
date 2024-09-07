package com.example.weatherapp.Repository

import com.example.weatherapp.Server.ApiServices

class CityRepository(val api: ApiServices){
    fun getCities(q:String,limit:Int) =
        api.getCitiesList(q,limit,"3bd707a9fd7103fc174da48c8f3b7f02")
}