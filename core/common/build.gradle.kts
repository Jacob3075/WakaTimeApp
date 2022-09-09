plugins {
    id("wakatimeapp.android.library")
}

dependencies {
    implementation(project(":core:models"))

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // OAuth
    implementation("net.openid:appauth:0.11.1")

    // Room
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Retrofit extras
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    //    OkHTTP
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}
