plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.google.devtools.ksp") version Versions.ksp
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.jacob.wakatimeapp"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        vectorDrawables { useSupportLibrary = true }

        manifestPlaceholders["appAuthRedirectScheme"] = "wakatimeapp"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = Versions.compose }
    packagingOptions {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // https://github.com/mockk/mockk/issues/297#issuecomment-901924678
    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    // Compose
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")

    // Androidx Core
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.core:core-splashscreen:1.0.0-alpha02")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    implementation("androidx.navigation:navigation-testing:${Versions.navigation}")

    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    // Room
    implementation("com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    ksp("androidx.room:room-compiler:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    testImplementation("androidx.room:room-testing:${Versions.room}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //    OkHTTP
    implementation(platform("com.squareup.okhttp3:okhttp-bom:${Versions.okhttp3}"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // OAuth
    implementation("net.openid:appauth:0.10.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:1.4.0")

    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Charts/Graphs
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lottie Animations
    implementation("com.airbnb.android:lottie-compose:4.2.0")

    // Core Testing
    testImplementation("junit:junit:${Versions.junit}")
    testImplementation("androidx.test.ext:junit-ktx:${Versions.extJunit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.extJunit}")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.archVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.compose}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Versions.espresso}")

    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.hilt}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp3}")
    androidTestImplementation("io.mockk:mockk-android:1.12.0")

    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")
}

kapt {
    correctErrorTypes = true
}
