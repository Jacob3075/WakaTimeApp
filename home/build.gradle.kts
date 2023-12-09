plugins {
    id("wakatimeapp.android.feature")
}

android {
    namespace = "com.jacob.wakatimeapp.home"
}

dependencies {
    // Image Loading, Charts, Lottie Animations
    implementation(libs.coil.kt.compose)
    implementation(libs.vico.compose.m3)

    implementation(libs.androidx.dataStore.preferences)
}
