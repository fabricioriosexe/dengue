package com.dengueserver.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RiskData(
    val id: String? = null,
    val date: String,
    val location: GeoPoint,
    val zoneFactor: Double,
    val currentCases: Int,
    val rain7Days: Double,
    val avgHumidity: Double,
    val calculatedRisk: Double
)
