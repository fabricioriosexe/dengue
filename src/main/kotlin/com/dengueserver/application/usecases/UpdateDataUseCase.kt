package com.dengueserver.application.usecases

import com.dengueserver.domain.models.GeoPoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ElevationResponse(val results: List<ElevationResult>)

@Serializable
data class ElevationResult(val elevation: Double)

class UpdateDataUseCase(
    private val calculateRiskUseCase: CalculateRiskUseCase
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun execute() {
        // Mock points representing different zones in Misiones
        val points = listOf(
            GeoPoint(-27.36708, -55.89608), // Posadas
            GeoPoint(-25.59912, -54.57355), // Puerto Iguazu
            GeoPoint(-27.4806, -54.8049),   // Obera
            GeoPoint(-26.4063, -54.6713)    // Montecarlo
        )
        
        for (point in points) {
            val elevation = fetchElevation(point)
            
            // Factor topográfico: A menor elevación, mayor probabilidad de agua estancada (factor más alto)
            // Elevación baja (<150m) -> factor alto (0.8)
            // Elevación media (150-300m) -> factor medio (0.5)
            // Elevación alta (>300m) -> factor bajo (0.2)
            val zoneFactor = when {
                elevation < 150.0 -> 0.8
                elevation < 300.0 -> 0.5
                else -> 0.2
            }
            
            calculateRiskUseCase.execute(point, zoneFactor)
        }
    }

    private suspend fun fetchElevation(point: GeoPoint): Double {
        return try {
            val url = "https://api.open-elevation.com/api/v1/lookup?locations=${point.latitude},${point.longitude}"
            val response: ElevationResponse = client.get(url).body()
            response.results.firstOrNull()?.elevation ?: 200.0 // Default 200m
        } catch (e: Exception) {
            // Fallback known elevations roughly
            if (point.latitude < -26.5) 120.0 else 250.0 
        }
    }
}
