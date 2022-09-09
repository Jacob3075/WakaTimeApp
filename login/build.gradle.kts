import java.util.*

plugins {
    id("wakatimeapp.android.feature")
}

android.buildTypes.forEach {
    val properties = loadPropertiesFile()

    it.buildConfigField(
        type = "String",
        name = "CLIENT_ID",
        value = "\"${properties.getProperty("CLIENT_ID")}\""
    )
    it.buildConfigField(
        type = "String",
        name = "CLIENT_SECRET",
        value = "\"${properties.getProperty("CLIENT_SECRET")}\""
    )
}

dependencies {
    // OAuth
    implementation("net.openid:appauth:0.11.1")

    // Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

fun loadPropertiesFile(): Properties {
    val propertiesFile = file("../local.properties")
    return Properties().apply { load(propertiesFile.inputStream()) }
}
