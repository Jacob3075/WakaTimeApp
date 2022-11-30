@file:Suppress("UnstableApiUsage")

plugins {
    id("wakatimeapp.android.library")
}

android {
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() }

    kotlinOptions { jvmTarget = "11" }
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
    implementation(libs.androidx.compose.ui.core)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.lottie.compose)

    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
}
