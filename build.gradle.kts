plugins {
    kotlin("jvm") version "2.0.21"
    alias(libs.plugins.ktor)
    alias(libs.plugins.graphql)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(libs.ktor.core)
    implementation(libs.ktor.netty)
    implementation(libs.logback)

    implementation(libs.arrow.core)

    implementation(libs.graphql.ktor)
    implementation(libs.graphql.hooks.provider)
}

kotlin {
    jvmToolchain(21)
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

graphql {
    schema {
        packages = listOf("com.example")
    }
}