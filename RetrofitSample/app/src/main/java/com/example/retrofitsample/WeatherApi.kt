package com.example.retrofitsample

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather/")
    fun getDataByCityName(
        @Query("q") cityName: String?,
        @Query("appid") APIKey: String?,
        @Query("units") units: String?,
        @Query("lang") language: String?,
        @Query("void") null_: String?
    ): Call<WeatherData?>?

    @GET("data/2.5/weather/")
    fun getDataByCityID(
        @Query("id") cityID: String?,
        @Query("appid") APIKey: String?,
        @Query("units") units: String?,
        @Query("lang") language: String?,
        @Query("void") null_: String?
    ): Call<WeatherData?>?

    @GET("data/2.5/weather/")
    fun getDataByCoordinates(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("appid") APIKey: String?,
        @Query("units") units: String?,
        @Query("lang") language: String?
    ): Call<WeatherData?>?

    @GET("data/2.5/weather/")
    fun getDataByZipCode(
        @Query("zip") zipCode: String?,
        @Query("appid") APIKey: String?,
        @Query("units") units: String?,
        @Query("lang") language: String?,
        @Query("void") null_: String?,
    ): Call<WeatherData?>?
}