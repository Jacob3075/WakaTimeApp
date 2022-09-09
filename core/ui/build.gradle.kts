plugins {
    id("wakatimeapp.android.library")
}

android {
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.1.1" }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:models"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")

    // Compose
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.activity:activity-compose:1.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
