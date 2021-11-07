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
    dynamicFeatures.add(":details")
}

dependencies {
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.appcompat:appcompat:1.3.1")
    api("com.google.android.material:material:1.4.0")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    // Compose
    api("androidx.compose.ui:ui:${Versions.compose}")
    api("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    api("androidx.compose.material:material:${Versions.compose}")
    api("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    api("androidx.activity:activity-compose:1.4.0")

    // Androidx Core
    api("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}")
    api("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
    api("androidx.legacy:legacy-support-v4:1.0.0")
    api("androidx.core:core-splashscreen:1.0.0-alpha02")
    api("androidx.datastore:datastore-preferences:1.0.0")

    // Navigation
    api("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
    api("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
    api("androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}")

    api("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Hilt
    api("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    // Room
    api("com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}")
    api("androidx.room:room-runtime:${Versions.room}")
    ksp("androidx.room:room-compiler:${Versions.room}")
    api("androidx.room:room-ktx:${Versions.room}")

    // Retrofit
    api("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    api("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //    OkHTTP
    api(platform("com.squareup.okhttp3:okhttp-bom:${Versions.okhttp3}"))
    api("com.squareup.okhttp3:okhttp")
    api("com.squareup.okhttp3:logging-interceptor")

    // OAuth
    api("net.openid:appauth:0.10.0")

    // Image Loading
    api("io.coil-kt:coil-compose:1.4.0")

    // Logging
    api("com.jakewharton.timber:timber:5.0.1")

    // Charts/Graphs
    api("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lottie Animations
    api("com.airbnb.android:lottie-compose:4.2.0")

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

    testImplementation("androidx.room:room-testing:${Versions.room}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.hilt}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.navigation}")
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
