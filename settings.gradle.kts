dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "Waka Time App"
include(":app")
include(":core")
include(":core:network")
include(":core:models")
include(":core:common")
include(":core:ui")
include(":core:database")
include(":details")
