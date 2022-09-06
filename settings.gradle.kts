dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "WakatimeApp.Compose"
include(":app")
include(":core")
include(":core:models")
include(":core:common")
include(":core:ui")
include(":core:data")
include(":details")
include(":home")
