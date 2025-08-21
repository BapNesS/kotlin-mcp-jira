plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.baptistecarlier.mcp.jira"
version = "1.0.0"

application {
    mainClass.set("com.baptistecarlier.mcp.jira.MainKt")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    jvmToolchain(20)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.modelcontextprotocol.sdk)
    implementation(libs.slf4j.simple)
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.mock)
}

tasks.test {
    useJUnitPlatform()
}