package com.dengueserver.infrastructure.adapters.epi

import com.dengueserver.domain.models.GeoPoint
import com.dengueserver.domain.ports.EpiPort
import kotlin.random.Random

class SimulatedEpiAdapter : EpiPort {
    override suspend fun getCurrentCases(location: GeoPoint): Int {
        // Simulación de casos del SNVS - Boletín Epidemiológico.
        // MVP: Datos aleatorios para demostrar el funcionamiento del modelo.
        return Random.nextInt(0, 500)
    }
}
