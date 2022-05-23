package com.example.retrofitsample

data class WeatherData (
    var coord: Coord,
    var weather: List<Weather>,
    var base: String,
    var main: Main,
    var visibility: Long,
    var wind: Wind,
    var clouds: Clouds,
    var dt: Long,
    var sys: Sys,
    var timezone: Int,
    var id: Long,
    var name: String,
    var cod: Int
)

data class Coord (
    val lon: Double,
    val lat: Double
        )

data class Weather (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
        )

data class Main (
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
        )

data class Wind (
    val speed: Double,
    val deg: Int,
    val gust: Double
        )

data class Clouds (
    val all: Int
        )

data class Sys (
    val type: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
        )