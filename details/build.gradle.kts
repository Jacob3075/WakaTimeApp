plugins {
    id("wakatimeapp.android.feature")
}

android {
    namespace = "com.jacob.wakatimeapp.details"
}

dependencies {
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.vico.compose.m3)
}
