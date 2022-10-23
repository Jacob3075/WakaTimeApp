plugins {
    id("wakatimeapp.android.feature")
}

android {
    namespace = "com.jacob.wakatimeapp.home"
}

dependencies {
    // Image Loading, Charts, Lottie Animations
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    annotationProcessor("androidx.room:room-compiler:2.4.3")
    ksp("androidx.room:room-compiler:2.4.3")
}
