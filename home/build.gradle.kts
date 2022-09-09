plugins {
    id("wakatimeapp.android.feature")
}

dependencies {
    // Image Loading, Charts, Lottie Animations
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.airbnb.android:lottie-compose:5.0.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
