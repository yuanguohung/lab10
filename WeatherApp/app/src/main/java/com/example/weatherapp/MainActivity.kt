package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etCity: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvWeatherResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        etCity = findViewById(R.id.etCity)
        btnSearch = findViewById(R.id.btnSearch)
        tvWeatherResult = findViewById(R.id.tvWeatherResult)

        // Set click listener on search button
        btnSearch.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                getWeather(city)
            }
        }
    }

    private fun getWeather(city: String) {
        val apiKey = "d232f504b5baccae1f078ca831533c62"  // Replace with your actual API Key
        val call = RetrofitClient.weatherService.getWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val temp = it.main.temp
                        val description = it.weather[0].description
                        val icon = it.weather[0].icon
                        val iconUrl = "https://openweathermap.org/img/w/$icon.png"

                        // Set weather information to the TextView
                        tvWeatherResult.text = "Temperature: $temp°C\nDescription: $description"

                        // Load weather icon using Glide
                        Glide.with(this@MainActivity)
                            .load(iconUrl)
                            .into(findViewById(R.id.ivWeatherIcon))  // Assuming you have an ImageView for the icon
                    }
                } else {
                    tvWeatherResult.text = "City not found!"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvWeatherResult.text = "Error: ${t.message}"
            }
        })
    }
}
