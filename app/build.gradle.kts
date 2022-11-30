plugins {
    id("wakatimeapp.android.application")
}

android {
    namespace = "com.jacob.wakatimeapp"
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

    // Core Testing
    testImplementation(libs.test.junit4)
    testImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    androidTestImplementation(libs.kotlinx.test.coroutines)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.test.mockk)
}
