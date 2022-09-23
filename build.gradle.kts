buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.7.10-1.0.6" apply false
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC1"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// This is a workaround for https://github.com/detekt/detekt/issues/3490
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins("ru.kode:detekt-rules-compose:1.2.1")
    }

    detekt {
        parallel = true
        config = files("${rootProject.rootDir}/tools/detekt-config.yml")
        buildUponDefaultConfig = true
    }

    tasks.detekt.configure {
        reports {
            md.required.set(true)
            md.outputLocation.set(
                file("${rootProject.rootDir}/tools/reports/${this@subprojects.name}-detekt.md")
            )
        }
        jvmTarget = "11"
    }
}
