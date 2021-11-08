
plugins {
    id("kotlin-android")
    id("com.android.dynamic-feature")
    id("androidx.navigation.safeargs.kotlin")
//    id("dagger.hilt.android.plugin") // TODO
    kotlin("kapt")
    id("com.google.devtools.ksp") version Versions.ksp
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = Versions.compose }
    packagingOptions {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":app"))

//    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
}
