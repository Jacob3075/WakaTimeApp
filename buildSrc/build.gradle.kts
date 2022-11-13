import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("groovy-gradle-plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>()
    .configureEach {
        kotlinOptions.jvmTarget = "11"
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
    implementation("com.android.tools.build:gradle:8.0.0-alpha08")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")

    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0-RC1")

    // https://github.com/google/dagger/issues/3068#issuecomment-999118496
    implementation("com.squareup:javapoet:1.13.0")
}
