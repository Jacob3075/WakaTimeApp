buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.plugin.gradle)
        classpath(libs.kotlin.plugin.gradle)
        classpath(libs.hilt.android.plugin.gradle)
        classpath(libs.test.mannodermaus.android.junit5)
    }
}

plugins {
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.compose.compiler) apply false
}
