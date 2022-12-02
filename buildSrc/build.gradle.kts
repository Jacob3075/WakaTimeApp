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
    implementation(libs.android.plugin.gradle)
    implementation(libs.kotlin.plugin.gradle)

    implementation(libs.kotlin.plugin.serialization)
    implementation(libs.ksp)

    implementation(libs.detekt.gradle.plugin)
    implementation(libs.gradle.version.plugin)

    // https://github.com/google/dagger/issues/3068#issuecomment-999118496
    //    implementation("com.squareup:javapoet:1.13.0")
}
