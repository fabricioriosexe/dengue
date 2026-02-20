package com.dengueserver.domain.ports

import com.dengueserver.domain.models.GeoPoint

interface EpiPort {
    suspend fun getCurrentCases(location: GeoPoint): Int
}
