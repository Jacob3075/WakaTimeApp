//plugins {
//    id("com.android.library")
//    id("kotlin-android")
//    id("androidx.navigation.safeargs.kotlin")
//    id("dagger.hilt.android.plugin")
//    kotlin("kapt")
//    id("com.google.devtools.ksp") version Versions.ksp
//    kotlin("plugin.serialization") version Versions.kotlin
//}
//
//android {
//    compileSdk = AppConfig.compileSdk
//
//    defaultConfig {
//        minSdk = AppConfig.minSdk
//        targetSdk = AppConfig.targetSdk
//
//        testInstrumentationRunner = AppConfig.androidTestInstrumentation
//        vectorDrawables { useSupportLibrary = true }
//    }
//
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions { jvmTarget = "11" }
//    buildFeatures { compose = true }
//    composeOptions { kotlinCompilerExtensionVersion = Versions.compose }
//    packagingOptions {
//        resources {
//            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
//        }
//    }
//}
//
//dependencies {
//    api("androidx.core:core-ktx:1.8.0")
//    api("androidx.appcompat:appcompat:${Versions.appcompat}")
//    api("com.google.android.material:material:${Versions.material}")
//
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
//
//    // Compose
//    api("androidx.compose.ui:ui:${Versions.compose}")
//    api("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
//    api("androidx.compose.material:material:${Versions.compose}")
//    api("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
//    api("androidx.activity:activity-compose:1.4.0")
//
//    // Androidx Core
//    api("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}")
//    api("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
//    api("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
//    api("androidx.legacy:legacy-support-v4:1.0.0")
//    api("androidx.core:core-splashscreen:1.0.0-rc01")
//    api("androidx.datastore:datastore-preferences:1.0.0")
//
//    // Navigation
//    api("androidx.navigation:navigation-fragment-ktx:${Versions.navigation}")
//    api("androidx.navigation:navigation-ui-ktx:${Versions.navigation}")
//    api("androidx.navigation:navigation-testing:${Versions.navigation}")
//
//    api("androidx.hilt:hilt-navigation-fragment:1.0.0")
//
//    // Hilt
//    api("com.google.dagger:hilt-android:${Versions.hilt}")
//    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
//
//    // Room
//    api("com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}")
//    api("androidx.room:room-runtime:${Versions.room}")
//    api("androidx.room:room-ktx:${Versions.room}")
//
//    // Image Loading
//    api("io.coil-kt:coil-compose:2.1.0")
//
//    // Logging
//    api("com.jakewharton.timber:timber:5.0.1")
//
//    // Charts/Graphs
//    api("com.github.PhilJay:MPAndroidChart:v3.1.0")
//
//    // Lottie Animations
//    api("com.airbnb.android:lottie-compose:5.0.3")
//
//    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
//    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.compose}")
//}
//
//kapt {
//    correctErrorTypes = true
//}
