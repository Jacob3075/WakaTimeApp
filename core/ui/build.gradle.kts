@file:Suppress("UnstableApiUsage")

plugins {
    id("wakatimeapp.android.library")
}

android {
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.3.1" }

    kotlinOptions { jvmTarget = "11" }
    namespace = "com.jacob.wakatimeapp.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:models"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.compose.material3:material3:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Compose
    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.0")

    implementation("com.airbnb.android:lottie-compose:5.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
