package com.example.retrofitsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cityNameField: TextView
    lateinit var weatherDescField: TextView
    lateinit var temperatureField: TextView
    lateinit var pressureHumidityField: TextView
    lateinit var coordsField: TextView
    lateinit var visibilityField: TextView
    lateinit var cloudField: TextView
    lateinit var windField: TextView
    lateinit var sunField: TextView
    lateinit var imageView: ImageView
    lateinit var sendButton: Button
    lateinit var keyField: TextView
    lateinit var responseField: TextView
    lateinit var text1: EditText
    lateinit var text2: EditText
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityNameField = findViewById(R.id.cityName)
        weatherDescField = findViewById(R.id.weatherDescription)
        temperatureField = findViewById(R.id.temperatures)
        pressureHumidityField = findViewById(R.id.pressure_humidity)
        coordsField = findViewById(R.id.coords)
        visibilityField = findViewById(R.id.visibility)
        cloudField = findViewById(R.id.cloudness)
        windField = findViewById(R.id.wind_data)
        sunField = findViewById(R.id.sun_data)
        imageView = findViewById(R.id.image)
        sendButton = findViewById(R.id.sendButton)
        keyField = findViewById(R.id.apiKey)
        responseField = findViewById(R.id.response)
        text1 = findViewById(R.id.text_1)
        text2 = findViewById(R.id.text_2)
        spinner = findViewById(R.id.spinner)

        text1.visibility = View.INVISIBLE
        text2.visibility = View.INVISIBLE
        cityNameField.visibility = View.INVISIBLE
        weatherDescField.visibility = View.INVISIBLE
        temperatureField.visibility = View.INVISIBLE
        pressureHumidityField.visibility = View.INVISIBLE
        coordsField.visibility = View.INVISIBLE
        visibilityField.visibility = View.INVISIBLE
        cloudField.visibility = View.INVISIBLE
        windField.visibility = View.INVISIBLE
        sunField.visibility = View.INVISIBLE
        imageView.visibility = View.INVISIBLE
        responseField.visibility = View.INVISIBLE


        val data = arrayOf(
            getString(R.string.name_filter),
            getString(R.string.id_filter),
            getString(R.string.coordinates_filter),
            getString(R.string.zip_code_filter)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        text1.visibility = View.VISIBLE
                        text1.hint = getString(R.string.City_name_label)
                        text2.visibility = View.INVISIBLE
                    }
                    1 -> {
                        text1.visibility = View.VISIBLE
                        text1.hint = getString(R.string.City_id_label)
                        text2.visibility = View.INVISIBLE
                    }
                    2 -> {
                        text1.visibility = View.VISIBLE
                        text1.hint = getString(R.string.latitude_label)
                        text2.visibility = View.VISIBLE
                        text2.hint = getString(R.string.longitude_label)
                    }
                    3 -> {
                        text1.visibility = View.VISIBLE
                        text1.hint = getString(R.string.zip_code_label)
                        text2.visibility = View.INVISIBLE
                    }
                }
                // показываем значение нажатого элемента
                //Toast.makeText(baseContext, "Choosed = ${data[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        //Nizhny ID: 520555
        // Nizhny coordinates: 56.2965,43.936

        sendButton.setOnClickListener {
            val apiKey: String = if (keyField.text.toString() == "") getString(R.string.API_KEY) else keyField.text.toString()
            when (spinner.selectedItem) {
                getString(R.string.name_filter) -> {
                    val name = text1.text.toString()
                    val values = Params(name, apiKey, "metric", getString(R.string.language_code), null)
                    val names = Params("name", "api key", "units", "language code", null)
                    retrofitRequest(App.api!!::getDataByCityName, values, names)
                }
                getString(R.string.id_filter) -> {
                    val id = text1.text.toString()
                    val values = Params(id, apiKey, "metric", getString(R.string.language_code),null)
                    val names = Params("city id", "api key", "units", "language code",null)
                    retrofitRequest(App.api!!::getDataByCityID, values, names)
                }
                getString(R.string.coordinates_filter) -> {
                    val latitude: String = text1.text.toString()
                    val longitude: String = text2.text.toString()
                    val values = Params(latitude, longitude, apiKey, "metric", getString(R.string.language_code))
                    val names = Params("latitude", "longitude", "api key", "units", "language code")
                    retrofitRequest(App.api!!::getDataByCoordinates, values, names)
                }
                getString(R.string.zip_code_filter) -> {
                    val zipCode = text1.text.toString()
                    val values = Params(zipCode, apiKey, "metric", getString(R.string.language_code), null)
                    val names = Params("zip code", "api key", "units", "language code", null)
                    retrofitRequest(App.api!!::getDataByZipCode, values, names)
                }
            }
        }
    }

    data class Params (
        val first: String?,
        val second: String?,
        val third: String?,
        val fourth: String?,
        val fifth: String?
    )

    fun retrofitRequest(requestMethod: (String?, String?, String?, String?, String?) -> Call<WeatherData?>?, param_values: Params, param_names: Params) {
        requestMethod(param_values.first, param_values.second, param_values.third, param_values.fourth, param_values.fifth)?.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                when (response.code()) {
                    404 -> {
                        val showText = if (param_values.fifth == null) "City with ${param_names.first} ${param_values.first} not found"
                        else "City with ${param_names.first} ${param_values.first} and ${param_names.second} ${param_values.second} not found"
                        Toast.makeText(
                            this@MainActivity,
                            showText,
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    400 -> {
                        val showText = if (param_values.fifth == null) "Empty ${param_names.first} field or just bad request"
                        else "Empty ${param_names.first} and ${param_names.second} fields or just bad request"
                        Toast.makeText(
                            this@MainActivity,
                            showText,
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    401 -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Bad API key, not authorized",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    200 -> {}
                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Unknown server answer, try again",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                }

                cityNameField.visibility = View.VISIBLE
                weatherDescField.visibility = View.VISIBLE
                temperatureField.visibility = View.VISIBLE
                pressureHumidityField.visibility = View.VISIBLE
                coordsField.visibility = View.VISIBLE
                visibilityField.visibility = View.VISIBLE
                cloudField.visibility = View.VISIBLE
                windField.visibility = View.VISIBLE
                sunField.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                val debug: Boolean = false
                if (debug) {
                    responseField.visibility = View.VISIBLE
                }

                responseField.text = response.body().toString()
                cityNameField.text = getString(R.string.city_name, response.body()?.name, response.body()?.sys?.country)
                weatherDescField.text = getString(R.string.weather_data, response.body()?.weather?.get(0)?.main, response.body()?.weather?.get(0)?.description)
                temperatureField.text = getString(R.string.temperature_data, response.body()?.main?.temp, response.body()?.main?.feels_like)
                pressureHumidityField.text = getString(R.string.pressure_humidity, response.body()?.main?.pressure, response.body()?.main?.humidity)
                coordsField.text = getString(R.string.coords, response.body()?.coord?.lat, response.body()?.coord?.lon)
                visibilityField.text = getString(R.string.visibility_data, response.body()?.visibility)
                cloudField.text = getString(R.string.cloud_data, response.body()?.clouds?.all)
                windField.text = getString(R.string.wind_data, response.body()?.wind?.speed)
                val dateFormat = "MM/dd/yyyy HH:mm:ss"
                val sunrise = response.body()?.sys?.sunrise
                val fixedSunrise = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date (sunrise!! * 1000))
                val sunset = response.body()?.sys?.sunset
                val fixedSunset = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date (sunset!! * 1000))
                sunField.text = getString(R.string.sun_data, fixedSunrise, fixedSunset)
                imageView.setBitmapFrom("http://openweathermap.org/img/wn/${response.body()?.weather?.get(0)?.icon}@4x.png")
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "An error with network occured",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}