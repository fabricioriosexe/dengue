package com.dengueserver.infrastructure.adapters.database

import com.dengueserver.domain.models.RiskData
import com.dengueserver.domain.ports.RiskRepository
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.eq

class MongoRiskRepository(connectionString: String) : RiskRepository {
    
    private val client = KMongo.createClient(connectionString).coroutine
    private val database = client.getDatabase("dengueserver")
    private val collection = database.getCollection<RiskData>("riskData")

    override suspend fun saveRiskData(data: RiskData) {
        collection.insertOne(data)
    }

    override suspend fun getRiskMap(date: String): List<RiskData> {
        return collection.find(RiskData::date eq date).toList()
    }
}
