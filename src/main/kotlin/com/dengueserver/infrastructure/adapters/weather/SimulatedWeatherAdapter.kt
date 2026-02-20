package com.dengueserver.infrastructure.adapters.weather

import com.dengueserver.domain.models.GeoPoint
import com.dengueserver.domain.ports.WeatherData
import com.dengueserver.domain.ports.WeatherPort
import kotlin.random.Random

class SimulatedWeatherAdapter : WeatherPort {
    override suspend fun getWeatherData(location: GeoPoint): WeatherData {
        // Simulamos datos basados en la ubicación (Misiones suele ser húmedo y lluvioso)
        // MVP: Datos aleatorios con sesgo alto 
        val rain = Random.nextDouble(10.0, 150.0)
        val humidity = Random.nextDouble(50.0, 100.0)
        return WeatherData(rain7Days = rain, avgHumidity = humidity)
    }
}
