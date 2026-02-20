package com.dengueserver

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.http.HttpMethod
import com.dengueserver.infrastructure.web.configureRouting
import com.dengueserver.infrastructure.adapters.database.MongoRiskRepository
import com.dengueserver.infrastructure.adapters.weather.OpenWeatherMapAdapter
import com.dengueserver.infrastructure.adapters.epi.SnvsEpiAdapter
import com.dengueserver.application.usecases.CalculateRiskUseCase
import com.dengueserver.application.usecases.GetRiskMapUseCase
import com.dengueserver.application.usecases.UpdateDataUseCase
import io.github.cdimascio.dotenv.dotenv

fun main() {
    val dotenv = dotenv {
        directory = "./"
        ignoreIfMissing = true
    }
    val portStr = dotenv["PORT"] ?: "8080"
    val port = portStr.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val dotenv = dotenv {
        directory = "./"
        ignoreIfMissing = true
    }
    
    val mongoUri = dotenv["MONGO_URI"] ?: "mongodb://localhost:27017"
    val openWeatherKey = dotenv["OPENWEATHER_API_KEY"] ?: "dummy_key_for_testing"
        
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(io.ktor.http.HttpHeaders.Authorization)
        allowHeader(io.ktor.http.HttpHeaders.ContentType)
    }

    val riskRepository = MongoRiskRepository(mongoUri)
    val weatherPort = OpenWeatherMapAdapter(openWeatherKey)
    val epiPort = SnvsEpiAdapter()

    val calculateRiskUseCase = CalculateRiskUseCase(weatherPort, epiPort, riskRepository)
    val getRiskMapUseCase = GetRiskMapUseCase(riskRepository)
    val updateDataUseCase = UpdateDataUseCase(calculateRiskUseCase)

    configureRouting(getRiskMapUseCase, updateDataUseCase)
}
