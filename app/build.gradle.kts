@file:Suppress("UnstableApiUsage")

plugins {
    id("wakatimeapp.android.application")
}

android {
    namespace = "com.jacob.wakatimeapp"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(project(":details"))
    implementation(project(":home"))
    implementation(project(":login"))

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.kotlinx.serialization.json)
}
