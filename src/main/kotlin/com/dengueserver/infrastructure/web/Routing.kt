package com.dengueserver.infrastructure.web

import com.dengueserver.application.usecases.GetRiskMapUseCase
import com.dengueserver.application.usecases.UpdateDataUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting(
    getRiskMapUseCase: GetRiskMapUseCase,
    updateDataUseCase: UpdateDataUseCase
) {
    routing {
        get("/") {
            call.respond(mapOf("status" to "ok", "message" to "Dengue Prediction Server is running!"))
        }

        get("/risk-map") {
            try {
                val geoJson = getRiskMapUseCase.execute()
                call.respond(HttpStatusCode.OK, geoJson)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }

        post("/update-data") {
            try {
                updateDataUseCase.execute()
                call.respond(HttpStatusCode.OK, mapOf("status" to "success", "message" to "Risk data updated successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }
    }
}
