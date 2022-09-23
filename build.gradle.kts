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
        // Builds the AST in parallel. Rules are always executed in parallel.
        // Can lead to speedups in larger projects. `false` by default.
        parallel = true

        // Define the detekt configuration(s) you want to use.
        // Defaults to the default detekt configuration.
        config = files("${project.rootDir}/tools/detekt-config.yml")

        // Applies the config files on top of detekt's default config file. `false` by default.
        buildUponDefaultConfig = true

        // Turns on all the rules. `false` by default.
        allRules = false
    }
}

tasks.detekt.configure {
    reports {
        // Enable/Disable HTML report (default: true)
        html.required.set(true)
        html.outputLocation.set(file("tools/reports/detekt.html"))

        // Enable/Disable MD report (default: false)
        md.required.set(true)
        md.outputLocation.set(file("tools/reports/detekt.md"))
    }
    jvmTarget = "11"
}
