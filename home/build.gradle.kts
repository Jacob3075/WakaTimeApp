plugins {
    id("wakatimeapp.android.feature")
}

android {
    namespace = "com.jacob.wakatimeapp.home"
}

dependencies {
    // Image Loading, Charts, Lottie Animations
    implementation(libs.coil.kt.compose)
    implementation(libs.philJay.mpAndroidChart)

    implementation(libs.androidx.dataStore.preferences)
}
