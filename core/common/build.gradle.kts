plugins {
    id("wakatimeapp.android.library")
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:data"))

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // OAuth
    implementation("net.openid:appauth:0.11.1")
}
