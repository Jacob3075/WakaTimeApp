import java.util.*

plugins {
    id("wakatimeapp.android.feature")
}
android {
    namespace = "com.jacob.wakatimeapp.login"
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
}

fun loadPropertiesFile(): Properties {
    val propertiesFile = file("../local.properties")
    return Properties().apply { load(propertiesFile.inputStream()) }
}
