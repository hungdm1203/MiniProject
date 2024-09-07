package com.example.weatherapp.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Adapter.ForecastAdapter
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.WeatherViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.CurrentResponseApi
import com.example.weatherapp.model.ForecastResponseApi
import com.github.matteobattilana.weather.PrecipType
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor= Color.TRANSPARENT
        }

        binding.apply {
//            var lat = 51.50
//            var lon =-0.12
//            var name="London"

            var lat=intent.getDoubleExtra("lat",0.0)
            var lon=intent.getDoubleExtra("lng",0.0)
            var name=intent.getStringExtra("name")

            if(lat==0.0){
                lat = 21.03
                lon = 105.85
                name = "Ha Noi"
            }

            addCity.setOnClickListener {
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

            cityTxt.text=name
            progressBar.visibility= View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                    override fun onResponse(
                        call: retrofit2.Call<CurrentResponseApi>,
                        response: retrofit2.Response<CurrentResponseApi>
                    ) {
                        if(response.isSuccessful){
                            val data=response.body()
                            progressBar.visibility= View.GONE
                            datailLayout.visibility= View.VISIBLE
                            data?.let {
                                statusTxt.text=it.weather?.get(0)?.main ?:"-"
                                windTxt.text=it.wind?.speed?.let { Math.round(it).toString() }+"Km"
                                currentTempTxt.text=it.main?.temp?.let { Math.round(it).toString() }+"°"
                                maxTempTxt.text=it.main?.tempMax?.let { Math.round(it).toString() }+"°"
                                minTempTxt.text=it.main?.tempMin?.let { Math.round(it).toString() }+"°"
                                humidityTxt.text=it.main?.humidity.toString()+"%"

                                val drawble= if(isNightNow()){
                                    R.drawable.night_bg
                                }else{
                                    setDynamiccallWallpaper(it.weather?.get(0)?.icon?:"-")
                                }
                                bgImage.setImageResource(drawble)
                                setEffectRainSnow(it.weather?.get(0)?.icon?:"-")
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<CurrentResponseApi>, t: Throwable) {
                        Toast.makeText(this@MainActivity,t.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            )


            var radius=10f
            var decorView=window.decorView
            var rootView=(decorView.findViewById(android.R.id.content) as ViewGroup?)
            var windowBackground=decorView.background

            rootView?.let {
                blueView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blueView.outlineProvider= ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline=true
            }

            weatherViewModel.loadForecastWeather(lat,lon,"metric")
                .enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                override fun onResponse(
                    call: retrofit2.Call<ForecastResponseApi>,
                    response: retrofit2.Response<ForecastResponseApi>
                ) {
                    if(response.isSuccessful) {
                        val data = response.body()
                        blueView.visibility = View.VISIBLE

                        data?.let {
                            forecastAdapter.differ.submitList(it.list)
                            forecastView.apply {
                                layoutManager= LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                                adapter=forecastAdapter
                            }
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ForecastResponseApi>, t: Throwable) {

                }

            })
        }
    }



    private fun isNightNow():Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY)>=18
    }

    private fun setDynamiccallWallpaper( icon:String ):Int{
        return when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "02","03","04" ->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09","10","11" ->{
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13" ->{
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50" ->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else ->0
        }
    }

    private fun setEffectRainSnow( icon:String ){
        when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "02","03","04" ->{
                initWeatherView(PrecipType.CLEAR)
            }
            "09","10","11" ->{
                initWeatherView(PrecipType.RAIN)
            }
            "13" ->{
                initWeatherView(PrecipType.SNOW)
            }
            "50" ->{
                initWeatherView(PrecipType.CLEAR)
            }
            else ->0
        }
    }

    private fun initWeatherView(type: PrecipType){
        binding.weatherView.apply {
            setWeatherData(type)
            angle=20
            emissionRate= 100.0f
        }
    }
}