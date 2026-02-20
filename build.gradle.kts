plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.dengueserver"
version = "0.0.1"

application {
    mainClass.set("com.dengueserver.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // MongoDB
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.11.0")
    
    // Dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}
