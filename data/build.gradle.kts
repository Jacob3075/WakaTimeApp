plugins {
    id("com.android.library")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.google.devtools.ksp") version Versions.ksp
    kotlin("plugin.serialization") version Versions.kotlin
}

android {
    compileSdk = AppConfig.compileSdk

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    // Room
    implementation("com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    annotationProcessor("androidx.room:room-compiler:${Versions.room}")
    ksp("androidx.room:room-compiler:${Versions.room}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    testImplementation("androidx.room:room-testing:${Versions.room}")

    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //    OkHTTP
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")


    // Core Testing
    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.extJunit}")
    implementation("com.jakewharton.timber:timber:5.0.1")
}

kapt {
    correctErrorTypes = true
}
