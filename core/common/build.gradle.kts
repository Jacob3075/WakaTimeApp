plugins {
    id("wakatimeapp.android.library")
}

android {
    namespace = "com.jacob.wakatimeapp.core.common"
}

dependencies {
    implementation(project(":core:models"))

    implementation(libs.androidx.lifecycle.runtimeKtx)

    // OAuth
    implementation(libs.appAuth)

    implementation(libs.androidx.dataStore.preferences)

    // Retrofit extras
    implementation(libs.retrofit.kotlin.serialization)

    //    OkHTTP
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
}
