package com.example.retrofitsample

import android.app.Application
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private var retrofit: Retrofit? = null
    override fun onCreate() {
        super.onCreate()
        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        weatherApi = retrofit?.create(WeatherApi::class.java)
    }

    companion object {
        private var weatherApi: WeatherApi? = null
        val api: WeatherApi?
            get() = weatherApi
    }
}