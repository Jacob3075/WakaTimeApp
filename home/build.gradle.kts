plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        vectorDrawables { useSupportLibrary = true }

        manifestPlaceholders["appAuthRedirectScheme"] = "wakatimeapp"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = Versions.compose }
}


dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.google.android.material:material:1.6.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-testing:${Versions.navigation}")

    // Compose
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // Lottie Animations
    implementation("com.airbnb.android:lottie-compose:5.0.3")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
