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

tasks.register<Jar>("buildFatJar") {
    archiveBaseName.set("dengueserver")
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { 
        attributes("Main-Class" to "com.dengueserver.ApplicationKt")
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
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
    
    // Ktor Client for external APIs
    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-cio-jvm")
    implementation("io.ktor:ktor-client-content-negotiation-jvm")
    implementation("org.apache.commons:commons-csv:1.10.0")
    
    // MongoDB
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:4.11.0")
    
    // Dotenv
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}
