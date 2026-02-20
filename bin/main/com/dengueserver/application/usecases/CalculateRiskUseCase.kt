package com.dengueserver.application.usecases

import com.dengueserver.domain.models.GeoPoint
import com.dengueserver.domain.models.RiskData
import com.dengueserver.domain.ports.EpiPort
import com.dengueserver.domain.ports.RiskRepository
import com.dengueserver.domain.ports.WeatherPort
import java.time.LocalDate

class CalculateRiskUseCase(
    private val weatherPort: WeatherPort,
    private val epiPort: EpiPort,
    private val riskRepository: RiskRepository
) {
    suspend fun execute(location: GeoPoint, zoneFactor: Double): RiskData {
        val weather = weatherPort.getWeatherData(location)
        val cases = epiPort.getCurrentCases(location)

        // Riesgo = (CasosActuales * 0.4) + (Lluvia7dias * 0.3) + (HumedadPromedio * 0.2) + (FactorZona * 0.1)
        val rawRisk = (cases * 0.4) + (weather.rain7Days * 0.3) + (weather.avgHumidity * 0.2) + (zoneFactor * 0.1)
        
        // Normalize risk to 0..1 for heatmap (assuming max raw risk could be around 100 for this MVP)
        val normalizedRisk = (rawRisk / 100.0).coerceIn(0.0, 1.0)
        
        val data = RiskData(
            date = LocalDate.now().toString(),
            location = location,
            zoneFactor = zoneFactor,
            currentCases = cases,
            rain7Days = weather.rain7Days,
            avgHumidity = weather.avgHumidity,
            calculatedRisk = normalizedRisk
        )
        
        riskRepository.saveRiskData(data)
        return data
    }
}
