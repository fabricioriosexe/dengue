package com.dengueserver.application.usecases

import com.dengueserver.domain.ports.RiskRepository
import java.time.LocalDate

class GetRiskMapUseCase(
    private val riskRepository: RiskRepository
) {
    suspend fun execute(): Map<String, Any> {
        val today = LocalDate.now().toString()
        val riskData = riskRepository.getRiskMap(today)
        
        val features = riskData.map { data ->
            mapOf(
                "type" to "Feature",
                "geometry" to mapOf(
                    "type" to "Point",
                    "coordinates" to listOf(data.location.longitude, data.location.latitude)
                ),
                "properties" to mapOf(
                    "risk" to data.calculatedRisk,
                    "cases" to data.currentCases,
                    "rain" to data.rain7Days,
                    "humidity" to data.avgHumidity
                )
            )
        }
        
        return mapOf(
            "type" to "FeatureCollection",
            "features" to features
        )
    }
}
