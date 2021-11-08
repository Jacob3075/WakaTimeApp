plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
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
    packagingOptions {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
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
    api("androidx.navigation:navigation-testing:${Versions.navigation}")

    api("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Room
    api("com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}")
    api("androidx.room:room-runtime:${Versions.room}")
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

    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")
}
