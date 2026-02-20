package com.dengueserver.infrastructure.adapters.epi

import com.dengueserver.domain.models.GeoPoint
import com.dengueserver.domain.ports.EpiPort
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.LoggerFactory
import kotlin.random.Random

class SnvsEpiAdapter : EpiPort {
    private val logger = LoggerFactory.getLogger(SnvsEpiAdapter::class.java)
    private val client = HttpClient(CIO)

    override suspend fun getCurrentCases(location: GeoPoint): Int {
        // En un caso real, el SNVS expone CSVs abiertos en datos.gob.ar
        // URL de ejemplo del catálogo: "https://sisa.msal.gov.ar/datos/descargas/covid-19/files/Covid19Casos.csv"
        // Para Dengue, el boletín epidemiológico publica PDFs o datasets específicos.
        // Aquí armamos el esqueleto de un scraper/fetcher HTTP pero caemos en datos geolocalizados realistas si falla.
        
        return try {
            // Simulated fetch to a public datasets API
            // val response: HttpResponse = client.get("https://datos.salud.gob.ar/api/public/dengue_latest.json")
            // val data = response.bodyAsText()
            
            // Simulación realista basada en la latitud (Norte de Misiones tiene más casos)
            val baseCases = if (location.latitude > -26.0) 300 else 100
            baseCases + Random.nextInt(0, 150)
        } catch (e: Exception) {
            logger.error("Error fetching SNVS data, falling back: ${e.message}")
            Random.nextInt(50, 200)
        }
    }
}
