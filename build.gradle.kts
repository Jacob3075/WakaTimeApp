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
    id("com.google.devtools.ksp") version "1.7.10-1.0.6" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
