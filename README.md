# Dengue Prediction Server MVP

Servidor de Predicción de Dengue para la provincia de Misiones, Argentina.  
Desarrollado en Kotlin con Ktor bajo una Arquitectura Hexagonal.

## Requisitos Extras
- **JDK 17+**
- **MongoDB** en ejecución.

## Configuración e Instalación
1. Clonar el repositorio.
2. Crear un archivo `.env` en la raíz del proyecto basándose en el siguiente ejemplo:
   ```env
   MONGO_URI=mongodb://localhost:27017
   PORT=8080
   OPENWEATHER_API_KEY=your_dummy_key
   ```
3. Ejecutar `./gradlew build` para construir la aplicación.
4. Ejecutar `./gradlew run` o cargar en IntelliJ IDEA e iniciar `ApplicationKt`.

## Endpoints
- `GET /risk-map`: Devuelve un objeto GeoJSON FeatureCollection con las coordenadas de zonas de Misiones y su valor de riesgo (0 a 1).
- `POST /update-data`: Pide a los adaptadores (clima y epidemiología simuados) los últimos datos y recalcula el riesgo, guardándolo en MongoDB.

## Arquitectura
El proyecto sigue una estructura Hexagonal con las siguientes capas:
- `domain`: Modelos core (GeoPoint, RiskData) y puertos (interfaces) para infraestructura externa.
- `application`: Casos de uso con la lógica del negocio.
- `infrastructure`: Adaptadores para APIs REST (Ktor), base de datos (MongoDB KMongo), etc.
