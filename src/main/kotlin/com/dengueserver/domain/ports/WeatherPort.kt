package com.dengueserver.domain.ports

import com.dengueserver.domain.models.GeoPoint

data class WeatherData(val rain7Days: Double, val avgHumidity: Double)

interface WeatherPort {
    suspend fun getWeatherData(location: GeoPoint): WeatherData
}
