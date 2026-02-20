package com.dengueserver.infrastructure.adapters.weather

import com.dengueserver.domain.models.GeoPoint
import com.dengueserver.domain.ports.WeatherData
import com.dengueserver.domain.ports.WeatherPort
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Serializable
data class OpenWeatherResponse(
    val main: MainData,
    val rain: RainData? = null
)

@Serializable
data class MainData(val humidity: Double)

@Serializable
data class RainData(val `1h`: Double? = null)

class OpenWeatherMapAdapter(private val apiKey: String) : WeatherPort {
    private val logger = LoggerFactory.getLogger(OpenWeatherMapAdapter::class.java)
    
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun getWeatherData(location: GeoPoint): WeatherData {
        if (apiKey == "dummy_key_for_testing" || apiKey.isEmpty()) {
            logger.warn("Using dummy API key for OpenWeather. Falling back to simulated data.")
            return WeatherData(rain7Days = 50.0, avgHumidity = 75.0)
        }

        return try {
            val url = "https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=$apiKey&units=metric"
            val response: OpenWeatherResponse = client.get(url).body()
            
            // OpenWeather standard endpoint gives 1h rain, we extrapolate or just use it as indicator for MVP
            val rainFound = response.rain?.`1h` ?: 0.0
            val estimated7DaysRain = rainFound * 24 * 7 // Crude estimation for MVP scaling
            
            WeatherData(
                rain7Days = estimated7DaysRain,
                avgHumidity = response.main.humidity
            )
        } catch (e: Exception) {
            logger.error("Error fetching weather from OpenWeatherMap: ${e.message}")
            // Fallback for MVP if API fails
            WeatherData(rain7Days = 0.0, avgHumidity = 60.0)
        }
    }
}
