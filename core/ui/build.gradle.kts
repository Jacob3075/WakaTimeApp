@file:Suppress("UnstableApiUsage")

plugins {
    id("wakatimeapp.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    buildFeatures { compose = true }

    namespace = "com.jacob.wakatimeapp.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:models"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    implementation(libs.kotlinx.coroutines.android)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.core)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.lottie.compose)
    implementation(libs.vico.compose.m3)

    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
}
