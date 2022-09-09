plugins {
    id("wakatimeapp.android.library")
}

android {
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.3.1" }

    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:models"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("com.google.android.material:material:1.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Compose
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
