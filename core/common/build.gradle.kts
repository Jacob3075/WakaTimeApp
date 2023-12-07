plugins {
    id("wakatimeapp.android.library")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.jacob.wakatimeapp.core.common"
}

dependencies {
    implementation(project(":core:models"))

    // OAuth
    implementation(libs.appAuth)

    implementation(libs.androidx.dataStore.preferences)

    // Retrofit extras
    implementation(libs.retrofit.kotlin.serialization)

    //    OkHTTP
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}

kapt {
    correctErrorTypes = true
}
