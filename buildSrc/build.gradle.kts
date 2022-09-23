plugins {
    `kotlin-dsl`
    id("groovy-gradle-plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0-RC1")
}
