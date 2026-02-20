package com.dengueserver.domain.ports

import com.dengueserver.domain.models.RiskData

interface RiskRepository {
    suspend fun saveRiskData(data: RiskData)
    suspend fun getRiskMap(date: String): List<RiskData>
}
