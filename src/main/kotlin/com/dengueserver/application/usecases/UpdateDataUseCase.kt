package com.dengueserver.application.usecases

import com.dengueserver.domain.models.GeoPoint

class UpdateDataUseCase(
    private val calculateRiskUseCase: CalculateRiskUseCase
) {
    suspend fun execute() {
        // Mock points representing different zones in Misiones
        val points = listOf(
            GeoPoint(-27.36708, -55.89608), // Posadas
            GeoPoint(-25.59912, -54.57355), // Puerto Iguazu
            GeoPoint(-27.4806, -54.8049),   // Obera
            GeoPoint(-26.1764, -54.6192)    // Eldorado
        )
        
        for (point in points) {
            // Factor topografico ponderado (estimado simple MVP)
            val zoneFactor = if (point.latitude < -26.5) 0.8 else 0.5 
            calculateRiskUseCase.execute(point, zoneFactor)
        }
    }
}
