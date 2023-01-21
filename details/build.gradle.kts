plugins {
    id("wakatimeapp.android.feature")
}

android {
    namespace = "com.jacob.wakatimeapp.details"
}

dependencies {
    implementation("com.google.accompanist:accompanist-pager:0.29.0-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.29.0-alpha")
}
