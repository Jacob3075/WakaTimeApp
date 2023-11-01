@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    id("wakatimeapp.android.feature")
}
android {
    namespace = "com.jacob.wakatimeapp.login"

    buildFeatures {
        buildConfig = true
    }
}

android.buildTypes.forEach {
    val properties = loadPropertiesFile()

    it.buildConfigField(
        type = "String",
        name = "CLIENT_ID",
        value = "\"${properties.getProperty("CLIENT_ID")}\"",
    )
    it.buildConfigField(
        type = "String",
        name = "CLIENT_SECRET",
        value = "\"${properties.getProperty("CLIENT_SECRET")}\"",
    )
}

dependencies {
    implementation(libs.appAuth)

    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.hilt.work)
}

fun loadPropertiesFile(): Properties {
    val propertiesFile = file("../local.properties")
    return Properties().apply { load(propertiesFile.inputStream()) }
}
